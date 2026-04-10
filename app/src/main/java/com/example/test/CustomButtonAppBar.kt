package com.example.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BottomBarItem(
    val icon: ImageVector,
    val contentDescription: String,
    val label: String,
    val onClick: () -> Unit = {}
)

@Composable
fun BottomBarItemView(item: BottomBarItem, modifier: Modifier = Modifier) {
    val iconButtonSize = 50.dp
    val iconSize = 30.dp
    val textFontSize = 14.sp

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = item.onClick, modifier = Modifier.size(iconButtonSize)) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = item.icon,
                contentDescription = item.contentDescription
            )
        }
        Text(
            fontSize = textFontSize,
            text = item.label
        )
    }
}

@Composable
fun CustomBottomBar(modifier: Modifier) {
    val spacerSize = 40.dp

    val items = listOf(
        BottomBarItem(icon = Icons.Default.Home, contentDescription = "Home page", label = "Home"),
        BottomBarItem(icon = Icons.Default.Add, contentDescription = "Create", label = "Create"),
        BottomBarItem(
            icon = Icons.Default.FolderOpen,
            contentDescription = "Library",
            label = "Library"
        ),
        BottomBarItem(
            icon = Icons.Default.Diamond,
            contentDescription = "Premium",
            label = "Premium"
        ),
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center
    ) {
        items.forEachIndexed { index, item ->
            BottomBarItemView(item = item)
            if (index < items.lastIndex) {
                Spacer(modifier = Modifier.size(spacerSize))
            }
        }
    }
}