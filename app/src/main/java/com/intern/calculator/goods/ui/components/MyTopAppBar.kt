package com.intern.calculator.goods.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.intern.calculator.goods.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String?,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector?,
    navigationIconContentDescription: String?,
    actionIcon: ImageVector?,
    actionIconContentDescription: String?,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.primary,
        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
) {
    var greeting = 0
    if (title.isNullOrBlank())
    {
        // Получение системного времени
        greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 4..11 -> R.string.main_appbar_greeting_1
            in 12..16 -> R.string.main_appbar_greeting_2
            in 17..23 -> R.string.main_appbar_greeting_3
            else -> R.string.main_appbar_greeting_4
        }
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (title.isNullOrBlank()) {
                    stringResource(greeting)
                } else {
                    title
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        modifier = modifier.testTag("MyTopAppBar"),
        colors = colors,
    )
}