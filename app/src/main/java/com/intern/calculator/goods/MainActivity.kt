package com.intern.calculator.goods

import CustomDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.intern.calculator.goods.ui.home.MyNavigationDrawerTitle
import com.intern.calculator.goods.ui.home.MyTopAppBar
import com.intern.calculator.goods.ui.theme.GoodsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoodsTheme {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Прикол
                    var presses by remember { mutableIntStateOf(0) }
                    val scope = rememberCoroutineScope()
                    // Состояние панели меню
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    // dummy list of menu items for example
                    val menuItems: List<CountdownMenu> = MenuItems()
                    // Выбранный элемент в меню
                    val selectedItem: MutableState<CountdownMenu> =
                        remember { mutableStateOf(menuItems[0]) }

                    // Активация возможности изменить/удалить списки
                    var couldChange by remember { mutableStateOf(false) }
                    var couldDelete by remember { mutableStateOf(false) }
                    // Значение, передающееся в модальное окно для изменения/удаления
                    var oldValue by remember { mutableStateOf("Test") }
                    // Значение, передающееся в модальное окно для выбора действия
                    var modalAction by remember { mutableStateOf(0) }
                    // Открыт/закрыт дмалог изменения удаления
                    val openDialogCustom = remember { mutableStateOf(false) }
                    // Открыт/закрыт дмалог отмены изменений
                    remember { mutableStateOf(false) }
                    val snackbarHostState = remember { SnackbarHostState() }
                    // Вызов диалога изменения/удаления
                    if (openDialogCustom.value) {
                        CustomDialog(
                            openDialogCustom = openDialogCustom,
                            oldValue = oldValue,
                            neededAction = modalAction,
                            onConfirmation = {
                                scope.launch {
                                    drawerState.apply { close() }
                                    val result = snackbarHostState
                                        .showSnackbar(
                                            message = when (modalAction) {
                                                0 -> getString(R.string.snackbar_text_action_0)
                                                1 -> getString(R.string.snackbar_text_action_1)
                                                else -> getString(R.string.snackbar_text_action_2)
                                            },
                                            actionLabel = getString(R.string.nav_drawer_modal_action_cancel_text),
                                            // Defaults to SnackbarDuration.Short
                                            duration = SnackbarDuration.Short
                                        )
                                    when (result) {
                                        SnackbarResult.ActionPerformed -> {
                                            /* Handle snackbar action performed */
                                            drawerState.apply { open()  } // TODO: Добавить логику
                                        }

                                        SnackbarResult.Dismissed -> {
                                            /* Handle snackbar dismissed */
                                            drawerState.apply { open() }
                                        }
                                    }
                                }
                            }
                        )
                    }

                    MaterialTheme {
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(
                                    drawerShape = RectangleShape,
                                    drawerTonalElevation = Dp(1F),
                                    content = {
                                        // Заголовок
                                        MyNavigationDrawerTitle(
                                            title = getString(R.string.nav_drawer_title),
                                            drawerState = drawerState
                                        )
                                        // Разделялка
                                        Divider()
                                        // Двигаемый список
                                        Column {
                                            LazyColumn(Modifier.weight(1f)) {
                                                items(menuItems) { item ->
                                                    // Каким будет каждый элемент
                                                    NavigationDrawerItem(
                                                        label = { Text(item.name) },
                                                        selected = item.index == selectedItem.value.index,
                                                        onClick = {
                                                            selectedItem.value = item
                                                            scope.launch { drawerState.close() }
                                                        },
                                                        badge = {
                                                            // Изменить
                                                            if (couldChange) {
                                                                IconButton(
                                                                    onClick = {
                                                                        oldValue = item.name
                                                                        openDialogCustom.value =
                                                                            true
                                                                        modalAction = 1
                                                                    },
                                                                ) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.Edit,
                                                                        contentDescription = "Close Navbar Drawer",
                                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                    )
                                                                }
                                                            }
                                                            // Удалить
                                                            if (couldDelete) {
                                                                IconButton(
                                                                    onClick = {
                                                                        oldValue = item.name
                                                                        openDialogCustom.value =
                                                                            true
                                                                        modalAction = 2
                                                                    },
                                                                ) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.Delete,
                                                                        contentDescription = "Close Navbar Drawer",
                                                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                            // Нижняя закрепленная строчка
                                            Box(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(56.dp)
                                                    .background(MaterialTheme.colorScheme.surface)
                                                    .padding(top = 4.dp)
                                            )
                                            {
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(start = 8.dp, end = 8.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    // Добавить
                                                    Column(Modifier.weight(1f)) {
                                                        OutlinedButton(
                                                            onClick = {
                                                                oldValue = ""
                                                                openDialogCustom.value =
                                                                    true
                                                                modalAction = 0
                                                            },
                                                            contentPadding = PaddingValues(
                                                                start = 8.dp,
                                                                end = 8.dp
                                                            ),
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Filled.Add,
                                                                contentDescription = "Close Navbar Drawer",
                                                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                            )
                                                            Text(
                                                                getString(R.string.nav_drawer_modal_action_0_approve),
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }
                                                    }
                                                    // Изменить
                                                    Column(Modifier.weight(1f)) {
                                                        OutlinedButton(
                                                            onClick = {
                                                                couldChange = !couldChange
                                                            },
                                                            contentPadding = PaddingValues(
                                                                start = 8.dp,
                                                                end = 8.dp
                                                            ),
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Filled.Edit,
                                                                contentDescription = "Close Navbar Drawer",
                                                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                            )
                                                            Text(
                                                                getString(R.string.nav_drawer_modal_action_1_approve),
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }
                                                    }
                                                    // Удалить
                                                    Column(Modifier.weight(1f)) {
                                                        OutlinedButton(
                                                            onClick = {
                                                                couldDelete = !couldDelete
                                                            },
                                                            contentPadding = PaddingValues(
                                                                start = 8.dp,
                                                                end = 8.dp
                                                            ),
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Filled.Delete,
                                                                contentDescription = "Close Navbar Drawer",
                                                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                            )
                                                            Text(
                                                                getString(R.string.nav_drawer_modal_action_2_approve),
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }
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
                                snackbarHost = {
                                    SnackbarHost(hostState = snackbarHostState)
                                },
                                topBar = {
                                    MyTopAppBar(
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
                                    modifier = Modifier.padding(innerPadding),
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