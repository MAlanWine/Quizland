package com.example.test.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.test.CustomBottomBar
import com.example.test.CustomTopBar
import com.example.test.data.FlashcardSetViewModel
import com.example.test.screens.create.CreateScreen
import com.example.test.screens.home.HomeScreen
import com.example.test.screens.library.LibraryScreen
import com.example.test.screens.premium.PremiumScreen
import com.example.test.screens.profile.ProfileScreen
import com.example.test.screens.profile.UserProfileViewModel

// ============================================================================
// Route definitions
// ============================================================================

enum class RootScreen(val route: String) {
    Main("main"),
    Profile("profile")
}

enum class TabScreen(val route: String) {
    Home("home"),
    Create("create"),
    Library("library"),
    Premium("premium")
}

// ============================================================================
// Animation timings
// ============================================================================

private const val TAB_SLIDE_MS = 300
private const val TAB_FADE_MS = 200

private const val ROOT_SLIDE_MS = 300

// ============================================================================
// Root-level transitions (traditional Activity slide: full-width, no fade)
// ============================================================================
// Push: new enters from +fullWidth, old exits to -fullWidth.
// Pop : new (parent) enters from -fullWidth, old (child) exits to +fullWidth.
// Fade is intentionally omitted — alpha compositing an entire screen is what
// causes frame drops; pure translation is GPU-cheap and matches the classic
// R.anim.slide_in_right / slide_out_left feel.

private val rootSpec = tween<IntOffset>(ROOT_SLIDE_MS, easing = FastOutSlowInEasing)

private fun rootPushEnter(): EnterTransition =
    slideInHorizontally(animationSpec = rootSpec) { fullWidth -> fullWidth }

private fun rootPushExit(): ExitTransition =
    slideOutHorizontally(animationSpec = rootSpec) { fullWidth -> -fullWidth }

private fun rootPopEnter(): EnterTransition =
    slideInHorizontally(animationSpec = rootSpec) { fullWidth -> -fullWidth }

private fun rootPopExit(): ExitTransition =
    slideOutHorizontally(animationSpec = rootSpec) { fullWidth -> fullWidth }

// ============================================================================
// Tab-level transitions (directional horizontal slide)
// ============================================================================

private val tabOrder = listOf(
    TabScreen.Home.route,
    TabScreen.Create.route,
    TabScreen.Library.route,
    TabScreen.Premium.route
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabSlideEnter(): EnterTransition {
    val from = tabOrder.indexOf(initialState.destination.route)
    val to = tabOrder.indexOf(targetState.destination.route)
    return when {
        from == -1 || to == -1 -> fadeIn(tween(TAB_FADE_MS))
        to > from -> slideInHorizontally(tween(TAB_SLIDE_MS)) { fullWidth -> fullWidth }
        to < from -> slideInHorizontally(tween(TAB_SLIDE_MS)) { fullWidth -> -fullWidth }
        else -> fadeIn(tween(TAB_FADE_MS))
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabSlideExit(): ExitTransition {
    val from = tabOrder.indexOf(initialState.destination.route)
    val to = tabOrder.indexOf(targetState.destination.route)
    return when {
        from == -1 || to == -1 -> fadeOut(tween(TAB_FADE_MS))
        to > from -> slideOutHorizontally(tween(TAB_SLIDE_MS)) { fullWidth -> -fullWidth }
        to < from -> slideOutHorizontally(tween(TAB_SLIDE_MS)) { fullWidth -> fullWidth }
        else -> fadeOut(tween(TAB_FADE_MS))
    }
}

// ============================================================================
// Root NavHost — main vs profile
// ============================================================================

@Composable
fun AppNavHost(
    rootNavController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    flashcardSetViewModel: FlashcardSetViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = rootNavController,
        startDestination = RootScreen.Main.route,
        modifier = modifier,
        enterTransition = { rootPushEnter() },
        exitTransition = { rootPushExit() },
        popEnterTransition = { rootPopEnter() },
        popExitTransition = { rootPopExit() }
    ) {
        composable(RootScreen.Main.route) {
            MainScaffold(
                userProfileViewModel = userProfileViewModel,
                flashcardSetViewModel = flashcardSetViewModel,
                onAvatarClick = {
                    rootNavController.navigate(RootScreen.Profile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(RootScreen.Profile.route) {
            ProfileScreen(
                viewModel = userProfileViewModel,
                onBack = { rootNavController.popBackStack() }
            )
        }
    }
}

// ============================================================================
// Main shell — Scaffold (TopBar + BottomBar) + inner tab NavHost
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(
    userProfileViewModel: UserProfileViewModel,
    flashcardSetViewModel: FlashcardSetViewModel,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentTabRoute = backStackEntry?.destination?.route

    val navigateToTab: (String) -> Unit = { route ->
        if (route != currentTabRoute) {
            tabNavController.navigate(route) {
                popUpTo(TabScreen.Home.route) {
                    inclusive = false
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = { CustomTopBar(onAvatarClick = onAvatarClick) },
        bottomBar = {
            BottomAppBar(
                actions = {
                    CustomBottomBar(
                        currentRoute = currentTabRoute,
                        onNavigate = navigateToTab
                    )
                }
            )
        }
    ) { innerPadding ->
        TabNavHost(
            navController = tabNavController,
            userProfileViewModel = userProfileViewModel,
            flashcardSetViewModel = flashcardSetViewModel,
            onSetCreated = { navigateToTab(TabScreen.Library.route) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// ============================================================================
// Inner tab NavHost — 4 bottom-bar destinations
// ============================================================================

@Composable
private fun TabNavHost(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    flashcardSetViewModel: FlashcardSetViewModel,
    onSetCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TabScreen.Home.route,
        modifier = modifier,
        enterTransition = { tabSlideEnter() },
        exitTransition = { tabSlideExit() },
        popEnterTransition = { tabSlideEnter() },
        popExitTransition = { tabSlideExit() }
    ) {
        composable(TabScreen.Home.route) {
            HomeScreen(
                userProfileViewModel = userProfileViewModel,
                flashcardSetViewModel = flashcardSetViewModel
            )
        }
        composable(TabScreen.Create.route) {
            CreateScreen(
                flashcardSetViewModel = flashcardSetViewModel,
                onSetCreated = onSetCreated
            )
        }
        composable(TabScreen.Library.route) {
            LibraryScreen(flashcardSetViewModel = flashcardSetViewModel)
        }
        composable(TabScreen.Premium.route) { PremiumScreen() }
    }
}
