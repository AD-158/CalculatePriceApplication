package com.intern.calculator.goods.ui.item.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.intern.calculator.goods.R
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.ui.AppViewModelProvider
import com.intern.calculator.goods.ui.components.CustomDialog
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.item.entry.formatedPrice
import com.intern.calculator.goods.ui.item.entry.toItem
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

// Destination for the item details screen
object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Collect item details UI state from the view model
    val uiState = viewModel.uiState.collectAsState()
    // Remember coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    // The undo dialog is opened/closed
    val snackbarHostState = remember { SnackbarHostState() }
    // Get the quantity unit list from the view model
    var quantityUnitList = viewModel.getQuantityUnitList()

    // Check if the quantity unit list is not empty
    if (quantityUnitList.isNotEmpty()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                MyTopAppBar(
                    title = stringResource(ItemDetailsDestination.titleRes),
                    navigationIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                    navigationIconContentDescription = "Navigate back",
                    actionIcon = null,
                    actionIconContentDescription = null,
                    onNavigationClick = navigateBack
                )
            },
            modifier = modifier
        ) { innerPadding ->
            // Body of the screen
            ItemDetailsBody(
                itemDetailsUiState = uiState.value,
                quantityUnitList = quantityUnitList,
                onEdit = { navigateToEditItem(uiState.value.itemDetails.id) },
                onDelete = {
                    // Note: If the user rotates the screen very fast, the operation may get cancelled
                    // and the item may not be deleted from the Database. This is because when config
                    // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                    // be cancelled - since the scope is bound to composition.

                    // Launch coroutine to delete item
                    coroutineScope.launch {
                        viewModel.deleteItem()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                snackbarHostState = snackbarHostState,
            )
        }
    } else {
        // If the quantity unit list is empty, show a loading indicator
        quantityUnitList = viewModel.getQuantityUnitList()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ItemDetailsBody(
    itemDetailsUiState: ItemDetailsUiState,
    quantityUnitList: List<QuantityUnit>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    // State for whether delete confirmation dialog is open
    val openDialogCustom = remember { mutableStateOf(true) }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // State for delete confirmation
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        // Display item details
        ItemDetails(
            item = itemDetailsUiState.itemDetails.toItem(),
            quantityUnitList = quantityUnitList,
            modifier = Modifier.fillMaxWidth(),
        )
        // Button to edit item
        Button(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_item),
            )
            Text(stringResource(R.string.edit_item))
        }
        // Button to delete item
        OutlinedButton(
            onClick = {
                deleteConfirmationRequired = true
                openDialogCustom.value = true
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_item),
            )
            Text(stringResource(R.string.delete_item))
        }
        // Show delete confirmation dialog if required
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp),
                item = itemDetailsUiState.itemDetails.toItem(),
                openDialogCustom = openDialogCustom,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

// Composable function to display item details
@Composable
fun ItemDetails(
    quantityUnitList: List<QuantityUnit>,
    item: Item,
    modifier: Modifier = Modifier
) {
    // Retrieve the application context
    val context = LocalContext.current
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display item details in rows
            // Item Name
            ItemDetailsRow(
                labelResID = R.string.item,
                itemDetail = item.name,
                modifier = Modifier.padding(
                    horizontal = 0.dp
                )
            )
            // Item Quantity
            ItemDetailsRow(
                labelResID = R.string.quantity,
                itemDetail = item.quantity.toString(),
                modifier = Modifier.padding(
                    horizontal = 0.dp
                )
            )
            // Item type of Quantity
            ItemDetailsRow(
                labelResID = R.string.quantity_type,
                itemDetail = context.getString(
                    quantityUnitList.first { it.id == item.quantityType }.name
                ),
                modifier = Modifier.padding(
                    horizontal = 0.dp
                )
            )
            // Item Price
            ItemDetailsRow(
                labelResID = R.string.price,
                itemDetail = item.formatedPrice(),
                modifier = Modifier.padding(
                    horizontal = 0.dp
                )
            )
        }

    }
}

// Composable function to display a row of item details
@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

// Composable function to display delete confirmation dialog
@Composable
private fun DeleteConfirmationDialog(
    item: Item,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    openDialogCustom: MutableState<Boolean>,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    // Remember coroutine scope for launching coroutines
    val coroutineScope = rememberCoroutineScope()
    // Retrieve the application context
    val context = LocalContext.current
    if (openDialogCustom.value) {
        CustomDialog(
            oldValue = item.name,
            neededAction = 2,
            onConfirmation = {
                openDialogCustom.value = false
                coroutineScope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = context.getString(R.string.snackbar_text_action_2),
                            actionLabel = context.getString(R.string.nav_drawer_modal_action_cancel_text),
                            // Defaults to SnackbarDuration.Short
                            duration = SnackbarDuration.Short
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            /* Handle snackbar approved */
                            onDeleteCancel()
                        }

                        SnackbarResult.Dismissed -> {
                            /* Handle snackbar dismissed */
                            onDeleteConfirm()
                        }
                    }
                }
            },
            onCancel = {
                openDialogCustom.value = false
                onDeleteCancel()
            },
        )
    }
}
