package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.test.data.FlashcardSetViewModel
import com.example.test.navigation.AppNavHost
import com.example.test.screens.profile.UserProfileViewModel
import com.example.test.ui.theme.AppTheme

val unionVerticalPaddingValue = 20.dp

class TestAPP : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val rootNavController = rememberNavController()
                val userProfileViewModel: UserProfileViewModel = viewModel()
                val flashcardSetViewModel: FlashcardSetViewModel = viewModel()
                AppNavHost(
                    rootNavController = rootNavController,
                    userProfileViewModel = userProfileViewModel,
                    flashcardSetViewModel = flashcardSetViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                            placeholder = { Text("Search") }
                        )
                    },
                    expanded = false,
                    onExpandedChange = {}
                ) {}
                Spacer(modifier = Modifier.size(5.dp))
                IconButton(
                    onClick = onAvatarClick,
                    modifier = Modifier.size(53.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Open profile",
                        modifier = Modifier.size(53.dp)
                    )
                }
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
