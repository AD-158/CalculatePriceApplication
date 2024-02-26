package com.intern.calculator.goods.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Composable function to display a title for the navigation drawer.
 * @param title The title text to be displayed.
 * @param drawerState The state of the drawer, used to open and close it.
 */
@Composable
fun MyNavigationDrawerTitle(title: String, drawerState: DrawerState) {
    // Remember coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    // Row to contain the title text and the close button
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Title text
        Text(
            title,
            modifier = Modifier.padding(12.dp),
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge
        )
        // Close button to close the drawer
        IconButton(
            onClick = {
                // Toggle the drawer state (open/close)
                coroutineScope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            // Icon for the close button
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Close Navbar Drawer"
            )
        }
    }
}