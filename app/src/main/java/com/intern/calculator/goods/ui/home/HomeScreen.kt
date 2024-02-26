package com.intern.calculator.goods.ui.home

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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import com.intern.calculator.goods.data.classes.Category
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.ui.AppViewModelProvider
import com.intern.calculator.goods.ui.components.CustomDialog
import com.intern.calculator.goods.ui.components.MyNavigationDrawerTitle
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.item.entry.formatedPrice
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import com.intern.calculator.goods.ui.settings.SettingsViewModel
import com.intern.calculator.goods.ui.settings.Theme
import com.intern.calculator.goods.ui.settings.UserPreferences
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

// Destination for the home screen
object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

// Composable function for the HomeScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: (Int) -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    // Remember coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    // State for drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Collect user preferences state
    val userPreferences by settingsViewModel.userPreferences.collectAsState(
        initial = UserPreferences(
            Theme.System,
            settingsViewModel.toLanguage(Locale.getDefault().language),
            1)
    )

    // Collect home UI state
    val homeUiState by viewModel.homeUiState.collectAsState()
    // Retrieve the application context
    val context = LocalContext.current

    // Menu items
    var menuItems = viewModel.getCategoryList()

    // State for enabling change/delete lists
    var couldChange by remember { mutableStateOf(false) }
    var couldDelete by remember { mutableStateOf(false) }
    // Values for dialogs
    var oldValue by remember { mutableStateOf("Test") }
    var newValue by remember { mutableStateOf("Test") }
    // The value passed to the modal window for selecting an action
    var modalAction by remember { mutableIntStateOf(0) }
    // State for opening/closing custom dialog
    val openDialogCustom = remember { mutableStateOf(false) }
    // State for snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    var needReload by remember { mutableStateOf(false) }


    // Selected item in the menu
    var selectedItem by remember { mutableIntStateOf(userPreferences.selectedList) }
    LaunchedEffect(key1 = userPreferences) {
        selectedItem = userPreferences.selectedList
    }
    viewModel.updateItemListBasedOnId(selectedItem)
    var quantityUnitList = viewModel.getQuantityUnitList()

    // Display dialog for change/delete
    if (openDialogCustom.value) {
        CustomDialog(
            oldValue = oldValue,
            neededAction = modalAction,
            onConfirmation = {
                data-> newValue = data
                openDialogCustom.value = false
                coroutineScope.launch {
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
                                    val filteredList = menuItems.filter { it.id != (
                                            menuItems.first { item -> item.name == oldValue }.id) }
                                    if (selectedItem == (menuItems.first { it.name == oldValue }.id)) {
                                        selectedItem = ((menuItems.first().id))
                                    }
                                    settingsViewModel.updateSelectedList(selectedItem)
                                    if (filteredList.isEmpty()) {
                                        viewModel.resetAutoIncrement("t_category")
                                        viewModel.resetAutoIncrement("t_item")
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

    // Reload menu items if needed
    if (needReload) {
        menuItems = viewModel.getCategoryList()
        needReload = false
    }

    // Check if menu items and quantity unit list are not empty
    if (menuItems.isNotEmpty() and quantityUnitList.isNotEmpty()) {
        MaterialTheme {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerShape = RectangleShape,
                        drawerTonalElevation = Dp(1F),
                        content = {
                            // Drawer content
                            // Navigation drawer title
                            MyNavigationDrawerTitle(
                                title = context.getString(R.string.nav_drawer_title),
                                drawerState = drawerState
                            )
                            // Divider
                            HorizontalDivider()
                            // Scrollable column for menu items
                            Column {
                                LazyColumn(
                                    Modifier
                                        .weight(1f)
                                        .padding(8.dp)) {
                                    items(menuItems) { item ->
                                        // Navigation drawer item
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
                                                coroutineScope.launch {
                                                    settingsViewModel.updateSelectedList(selectedItem)
                                                    drawerState.close()
                                                }
                                            },
                                            badge = {
                                                // Edit button
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
                                                // Delete button
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
                                // Bottom row
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
                                        // Add button
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
                                        // Edit button
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
                                        // Delete button
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
                                coroutineScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            onActionClick = navigateToSettings
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
                    // Body of the screen
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
                            coroutineScope.launch {
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
    // If menu items or quantity unit list are empty, display loading indicator
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

// Composable function for the body of the screen
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
        // Display message if item list is empty, otherwise display item list
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

// Composable function for displaying inventory list
@Composable
private fun InventoryList(
    itemList: List<Item>,
    quantityUnitList: List<QuantityUnit>,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Find item with the lowest price per unit
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

// Composable function for displaying inventory item
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