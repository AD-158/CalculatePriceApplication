package com.intern.calculator.goods

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.intern.calculator.goods.ui.home.MyNavigationDrawerTitle
import com.intern.calculator.goods.ui.home.MyTopAppBar
import com.intern.calculator.goods.ui.theme.GoodsTheme
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Calendar

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoodsTheme {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var presses by remember { mutableIntStateOf(0) }
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    // dummy list of menu items for example
                    val menuItems: List<CountdownMenu> = MenuItems()

                    val selectedItem: MutableState<CountdownMenu> =
                        remember { mutableStateOf(menuItems[0]) }

                    // Получение системного времени
                    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        in 4..11 -> R.string.main_appbar_greeting_1
                        in 12..16 -> R.string.main_appbar_greeting_2
                        in 17..23 -> R.string.main_appbar_greeting_3
                        else -> R.string.main_appbar_greeting_4
                    }

                    MaterialTheme {
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(
                                    drawerShape = RectangleShape,
                                    drawerTonalElevation = Dp(1F),
                                    content = {
                                        MyNavigationDrawerTitle(
                                            title = getString(R.string.nav_drawer_title),
                                            drawerState = drawerState
                                        )
                                        Divider()
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Column {
                                                LazyColumn {
                                                    items(menuItems) { item ->
                                                        NavigationDrawerItem(
                                                            label = { Text(item.name) },
                                                            selected = item.index == selectedItem.value.index,
                                                            onClick = {
                                                                selectedItem.value = item
                                                                scope.launch { drawerState.close() }
                                                            },
                                                            badge = {
                                                                IconButton(
                                                                    onClick = { presses++ },
                                                                ) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.Edit,
                                                                        contentDescription = "Close Navbar Drawer",
                                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                    )
                                                                }
                                                                IconButton(
                                                                    onClick = { presses++ },
                                                                ) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.Delete,
                                                                        contentDescription = "Close Navbar Drawer",
                                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }
                                                    item {
                                                        Divider()
                                                    }
                                                    item {
                                                        NavigationDrawerItem(
                                                            label = { Text(text = "Drawer Item") },
                                                            selected = false,
                                                            onClick = { /*TODO*/ }
                                                        )
                                                    }
                                                    item {
                                                        Spacer(modifier = Modifier.height(16.dp))
                                                    }
                                                }
                                            }
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(4.dp),
                                                horizontalAlignment = Alignment.End,
                                                verticalArrangement = Arrangement.Bottom,
                                            ) {
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                ) {
                                                    SmallFloatingActionButton(onClick = { presses++ }) {
                                                        Icon(
                                                            Icons.Default.Add,
                                                            contentDescription = "Add",
                                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                        )
                                                    }
                                                    SmallFloatingActionButton(onClick = { presses++ }) {
                                                        Icon(
                                                            Icons.Default.Edit,
                                                            contentDescription = "Add",
                                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                        )
                                                    }
                                                    SmallFloatingActionButton(onClick = { presses++ }) {
                                                        Icon(
                                                            Icons.Default.Delete,
                                                            contentDescription = "Add",
                                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            },
                            gesturesEnabled = true,
                        ) {
                            Scaffold(
                                topBar = {
                                    MyTopAppBar(
                                        title = getString(greeting),
                                        navigationIcon = Icons.Outlined.Menu,
                                        navigationIconContentDescription = "Navigation icon",
                                        actionIcon = Icons.Outlined.Settings,
                                        actionIconContentDescription = "Action icon",
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                                            navigationIconContentColor = MaterialTheme.colorScheme.primary,
                                            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        ),
                                        onNavigationClick = {
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                    )
                                },
                                bottomBar = {},
                                floatingActionButton = {
                                    FloatingActionButton(onClick = { presses++ }) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Add",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            ) { innerPadding ->
                                Column(
                                    modifier = androidx.compose.ui.Modifier
                                        .padding(innerPadding),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                ) {
                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text =
                                        """
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.

                    You have pressed the floating action button $presses times.
                """.trimIndent(),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class CountdownMenu(index: Int, name: String) {
    var index: Int = index
    var name: String = name
}

fun MenuItems(): List<CountdownMenu> {
    val menuItems = mutableListOf<CountdownMenu>()
    for (i in 1..26) {
        menuItems.add(CountdownMenu(i, "Kolbasa Pyaterochka " + i))
    }
    return menuItems
}