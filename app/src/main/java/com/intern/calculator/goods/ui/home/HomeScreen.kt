package com.intern.calculator.goods.ui.home

import com.intern.calculator.goods.ui.components.CustomDialog
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.intern.calculator.goods.R
import com.intern.calculator.goods.data.Category
import com.intern.calculator.goods.data.Item
import com.intern.calculator.goods.data.QuantityUnit
import com.intern.calculator.goods.ui.AppViewModelProvider
import com.intern.calculator.goods.ui.components.MyNavigationDrawerTitle
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.item.formatedPrice
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.text.NumberFormat

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToItemEntry: (Int) -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    // Состояние панели меню
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val homeUiState by viewModel.homeUiState.collectAsState()
    val context = LocalContext.current

    var menuItems = viewModel.getCategoryList()

    // Выбранный элемент в меню
    var selectedItem by remember { mutableIntStateOf(1) }
    viewModel.updateItemListBasedOnId(selectedItem)
    var quantityUnitList = viewModel.getQuantityUnitList()

    // Активация возможности изменить/удалить списки
    var couldChange by remember { mutableStateOf(false) }
    var couldDelete by remember { mutableStateOf(false) }
    // Значение, передающееся в модальное окно для изменения/удаления
    var oldValue by remember { mutableStateOf("Test") }
    var newValue by remember { mutableStateOf("Test") }
    // Значение, передающееся в модальное окно для выбора действия
    var modalAction by remember { mutableIntStateOf(0) }
    // Открыт/закрыт диалог изменения удаления
    val openDialogCustom = remember { mutableStateOf(false) }
    // Открыт/закрыт диалог отмены изменений
    val snackbarHostState = remember { SnackbarHostState() }

    var needReload by remember { mutableStateOf(false) }

    // Вызов диалога изменения/удаления
    if (openDialogCustom.value) {
        CustomDialog(
            oldValue = oldValue,
            neededAction = modalAction,
            onConfirmation = {
                data-> newValue = data
                openDialogCustom.value = false
                scope.launch {
                    drawerState.apply { close() }
                    val result = snackbarHostState
                        .showSnackbar(
                            message = when (modalAction) {
                                0 -> context.getString(R.string.snackbar_text_action_0)
                                1 -> context.getString(R.string.snackbar_text_action_1)
                                else -> context.getString(R.string.snackbar_text_action_2)
                            },
                            actionLabel = context.getString(R.string.nav_drawer_modal_action_cancel_text),
                            duration = SnackbarDuration.Short
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            drawerState.apply { open()  }
                        }
                        SnackbarResult.Dismissed -> {
                            when (modalAction) {
                                0 -> {
                                    viewModel.createCategory(
                                        Category(
                                            name = newValue
                                        )
                                    )
                                }
                                1 -> {
                                    viewModel.updateCategory(
                                        Category(
                                            id = menuItems.first { it.name == oldValue }.id,
                                            name = newValue
                                        )
                                    )
                                }
                                else -> {
                                    viewModel.deleteCategory(menuItems.first { it.name == oldValue })
                                    menuItems.toMutableList().apply {
                                        removeIf { it.id == (
                                                menuItems.first { item -> item.name == oldValue }.id) }
                                    }
                                    if (selectedItem == (menuItems.first { it.name == oldValue }.id)) {
                                        selectedItem = ((menuItems.first().id))
                                    }
                                    if (menuItems.isEmpty()) {
                                        viewModel.createCategory(
                                            Category(
                                                id = 0,
                                                name = context.getString(R.string.first_list)
                                            )
                                        )
                                    }
                                }
                            }
                            needReload = true
                            drawerState.apply { open() }
                        }
                    }
                }
            },
            onCancel = {
                openDialogCustom.value = false
            }
        )
    }

    if (needReload) {
        menuItems = viewModel.getCategoryList()
        needReload = false
    }

    if (menuItems.isNotEmpty() and quantityUnitList.isNotEmpty()) {
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
                                title = context.getString(R.string.nav_drawer_title),
                                drawerState = drawerState
                            )
                            // Разделялка
                            Divider()
                            // Двигаемый список
                            Column {
                                LazyColumn(
                                    Modifier
                                        .weight(1f)
                                        .padding(8.dp)) {
                                    items(menuItems) { item ->
                                        // Каким будет каждый элемент
                                        NavigationDrawerItem(
                                            label = { Text(item.name) },
                                            selected = item.id ==
                                                    if (menuItems.firstOrNull { it.id == selectedItem } != null)
                                                        selectedItem
                                                    else {
                                                         menuItems.first().id
                                                    },
                                            onClick = {
                                                selectedItem = item.id
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
                                                    context.getString(R.string.nav_drawer_modal_action_0_approve),
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
                                                    context.getString(R.string.nav_drawer_modal_action_1_approve),
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
                                                    context.getString(R.string.nav_drawer_modal_action_2_approve),
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
                    topBar = {
                        MyTopAppBar(
                            title = null,
                            navigationIcon = Icons.Outlined.Menu,
                            navigationIconContentDescription = "Navigation icon",
                            actionIcon = Icons.Outlined.Settings,
                            actionIconContentDescription = "Action icon",
                            onNavigationClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navigateToItemEntry(selectedItem) },
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.item_entry_title)
                            )
                        }
                    },
                ) { innerPadding ->
                    HomeBody(
                        itemList = homeUiState.itemList,
                        quantityUnitList = quantityUnitList,
                        onItemClick = navigateToItemUpdate,
                        listName = menuItems.firstOrNull { it.id == (selectedItem) }?.name ?: menuItems.first().name,
                        modifier = modifier
                            .padding(innerPadding)
                            .padding(4.dp)
                            .fillMaxSize(),
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    else {
        menuItems = viewModel.getCategoryList()
        quantityUnitList = viewModel.getQuantityUnitList()
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            ) {
            CircularProgressIndicator()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBody(
    itemList: List<Item>,
    quantityUnitList: List<QuantityUnit>,
    onItemClick: (Int) -> Unit,
    listName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            onClick = onClick
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.home_top_card) + listName,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            InventoryList(
                itemList = itemList,
                quantityUnitList = quantityUnitList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = 0.dp),
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Item>,
    quantityUnitList: List<QuantityUnit>,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    val best = itemList.minBy { item -> ((item.price/item.quantity)
        *quantityUnitList.first { it.id == item.quantityType }.multiplier) }
    LazyColumn(modifier = modifier) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(
                item = item,
                quantityUnitList = quantityUnitList,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onItemClick(item) },
                containerColor = if (item.id == best.id) {
                    MaterialTheme.colorScheme.primary
                }
                else {

                    MaterialTheme.colorScheme.surface
                }
            )
        }
    }
}

@Composable
private fun InventoryItem(
    item: Item,
    quantityUnitList: List<QuantityUnit>,
    modifier: Modifier = Modifier,
    containerColor: Color
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
//                Spacer(Modifier.weight(1f))
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = item.formatedPrice(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Text(
                text = NumberFormat.getCurrencyInstance().format(
                    (item.price/item.quantity)
                            *quantityUnitList.first { it.id == item.quantityType }.multiplier
                ).toString() + stringResource(id = R.string.price_per_kg_or_l),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}