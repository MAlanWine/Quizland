package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.ui.theme.AppTheme

val unionVerticalPaddingValue = 20.dp

class TestAPP : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    topBar = { CustomTopBar() },
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier,
                            actions = { CustomBottomBar(modifier = Modifier) }
                        )
                    }
                ) { innerPadding ->
                    // Greeting("Name", Modifier.padding(innerPadding))
                    MainInterfacePart(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        actions = {
            Row(
                modifier = Modifier.padding(horizontal = unionVerticalPaddingValue),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var searchContent by remember { mutableStateOf("") }
                SearchBar(
                    modifier = Modifier.weight(1f),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchContent,
                            onQueryChange = { searchContent = it },
                            onSearch = {},
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = {
                                Text("Search")
                            }
                        )
                    },
                    expanded = false,
                    onExpandedChange = {}
                ) {}
                Spacer(modifier = Modifier.size(5.dp))
                // Avatar
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(53.dp),
                )
            }
        },
        title = { Text("") }
    )
}

@Composable
fun CusTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

// Main Interface part ==================================================================================================================

@Composable
fun MainInterfacePart(modifier: Modifier) {
    val sectionSpacing = 46.dp
    val titleToContentSpacing = 14.dp

    LazyColumn(
        modifier = modifier
            .padding(horizontal = unionVerticalPaddingValue)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
    ) {
        // Card
        item {
            CusTitle("Jump back in")
            Spacer(Modifier.size(titleToContentSpacing))
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(23.dp))
                    .clickable {}
                    .fillMaxWidth(),
                shape = RoundedCornerShape(23.dp)
            ) {
                Column(
                    modifier = Modifier.padding(27.dp)
                ) {
                    Text(
                        text = "Test Flash Cards",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(36.dp))
                    LinearProgressIndicator(
                        progress = { 1.0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp),
                        color = MaterialTheme.colorScheme.primary,
                        // color = ProgressIndicatorDefaults.linearColor,
                        trackColor = ProgressIndicatorDefaults.linearTrackColor,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = "2/2 cards sorted",
                        // fontSize = 24.dp
                    )
                    Spacer(Modifier.size(24.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        content = {
                            Text(
                                text = "Start questions",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                    )
                }
            }
        }

        // Recents
        item {
            CusTitle("Recents")
            Spacer(Modifier.size(titleToContentSpacing))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {}
                    .fillMaxWidth(),

                ) {
                Row(
                    modifier = Modifier.padding(vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp),
                        contentDescription = "Cards",
                        imageVector = Icons.Default.Style,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.size(10.dp))
                    Column(
                    ) {
                        Text(
                            text = "Test Flash Cards",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.size(2.dp))
                        Text(
                            text = "2 cards - by you",
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }

        // LazyRow version (laggy due to nested lazy lists, kept for reference)
//        item {
//            CusTitle("For your next study session")
//            Spacer(Modifier.size(titleToContentSpacing))
//            LazyRow(
//                modifier = Modifier.height(200.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                item {
//                    CustomCard2(
//                        title = "Common Quizland questions",
//                        cardCount = 15,
//                        author = "Quizland",
//                        studierCount = 112,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(23.dp))
//                            .clickable {}
//                    )
//                }
//                item {
//                    CustomCard2(
//                        title = "Quizland basics",
//                        cardCount = 11,
//                        author = "Quizland",
//                        studierCount = 497,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(23.dp))
//                            .clickable {}
//                    )
//                }
//                item {
//                    CustomCard2(
//                        title = "Effective study strategies",
//                        cardCount = 6,
//                        author = "Quizland",
//                        studierCount = 812,
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(23.dp))
//                            .clickable {}
//                    )
//                }
//            }
//        }

        // Row + horizontalScroll version (smoother)
        item {
            CusTitle("For your next study session")
            Spacer(Modifier.size(titleToContentSpacing))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomCard2(
                    title = "Common Quizland questions",
                    cardCount = 15,
                    author = "Quizland",
                    studierCount = 112,
                    modifier = Modifier
                        .clip(RoundedCornerShape(23.dp))
                        .clickable {}
                )
                CustomCard2(
                    title = "Quizland basics",
                    cardCount = 11,
                    author = "Quizland",
                    studierCount = 497,
                    modifier = Modifier
                        .clip(RoundedCornerShape(23.dp))
                        .clickable {}
                )
                CustomCard2(
                    title = "Effective study strategies",
                    cardCount = 6,
                    author = "Quizland",
                    studierCount = 812,
                    modifier = Modifier
                        .clip(RoundedCornerShape(23.dp))
                        .clickable {}
                )
            }
        }

        item {
            CusTitle("Try out these flashcard sets")
            Spacer(Modifier.size(titleToContentSpacing))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomEasyCard(
                    title = "Quizland basics",
                    cardCount = 11,
                    author = "Quizland",
                    modifier = Modifier
                        .width(240.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .clickable{}
                )
                CustomEasyCard(
                    title = "Common Quizland questions",
                    cardCount = 15,
                    author = "Quizland",
                    modifier = Modifier
                        .width(240.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .clickable{}
                )
                CustomEasyCard(
                    title = "Effective study stratefies",
                    cardCount = 12,
                    author = "Quizland",
                    modifier = Modifier
                        .width(240.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .clickable{}
                )
            }

            Spacer(Modifier.size(14.dp))
        }
    }
}