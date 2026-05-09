package com.example.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.navigation.TabScreen

data class BottomBarItem(
    val icon: ImageVector,
    val contentDescription: String,
    val label: String,
    val route: String
)

@Composable
fun BottomBarItemView(
    item: BottomBarItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = 28.dp
    val textFontSize = 14.sp

    val tint: Color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = item.icon,
            contentDescription = item.contentDescription,
            tint = tint
        )
        Spacer(Modifier.size(4.dp))
        Text(
            fontSize = textFontSize,
            text = item.label,
            color = tint
        )
    }
}

@Composable
fun CustomBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacerSize = 20.dp

    val items = listOf(
        BottomBarItem(
            icon = Icons.Default.Home,
            contentDescription = "Home page",
            label = "Home",
            route = TabScreen.Home.route
        ),
        BottomBarItem(
            icon = Icons.Default.Add,
            contentDescription = "Create",
            label = "Create",
            route = TabScreen.Create.route
        ),
        BottomBarItem(
            icon = Icons.Default.FolderOpen,
            contentDescription = "Library",
            label = "Library",
            route = TabScreen.Library.route
        ),
        BottomBarItem(
            icon = Icons.Default.Diamond,
            contentDescription = "Premium",
            label = "Premium",
            route = TabScreen.Premium.route
        ),
    )

    //

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        items.forEachIndexed { index, item ->
            BottomBarItemView(
                item = item,
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) }
            )
            if (index < items.lastIndex) {
                Spacer(modifier = Modifier.size(spacerSize))
            }
        }
    }
}
