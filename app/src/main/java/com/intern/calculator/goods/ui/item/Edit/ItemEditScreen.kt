package com.intern.calculator.goods.ui.item.Edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.intern.calculator.goods.R
import com.intern.calculator.goods.ui.AppViewModelProvider
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.item.Entry.ItemEntryBody
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ItemEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var quantityUnitList = viewModel.getQuantityUnitList()
    if (quantityUnitList.isNotEmpty()) {
        Scaffold(
            topBar = {
                MyTopAppBar(
                    title = stringResource(ItemEditDestination.titleRes),
                    navigationIcon = Icons.Outlined.ArrowBack,
                    navigationIconContentDescription = "Navigate back",
                    actionIcon = null,
                    actionIconContentDescription = null,
                    onNavigationClick = onNavigateUp
                )
            },
            modifier = modifier
        ) { innerPadding ->
            ItemEntryBody(
                itemUiState = viewModel.itemUiState,
                quantityUnitList = quantityUnitList,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    // Note: If the user rotates the screen very fast, the operation may get cancelled
                    // and the item may not be updated in the Database. This is because when config
                    // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                    // be cancelled - since the scope is bound to composition.
                    coroutineScope.launch {
                        if ((quantityUnitList.firstOrNull {
                                context.getString(it.name) ==
                                        viewModel.itemUiState.itemDetails.quantityType
                            }) != null) {
                            viewModel.updateUiState(
                                viewModel.itemUiState.itemDetails.copy(
                                    quantityType = quantityUnitList.first {
                                        context.getString(it.name) ==
                                                viewModel.itemUiState.itemDetails.quantityType
                                    }.id.toString()
                                )
                            )
                        }
                        viewModel.updateItem()
                        navigateBack()
                    }
                },
                modifier = Modifier.padding(innerPadding),
                buttonText = R.string.nav_drawer_modal_action_1_approve,
            )
        }
    }
    else {
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