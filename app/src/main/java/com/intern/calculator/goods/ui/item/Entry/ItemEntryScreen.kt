package com.intern.calculator.goods.ui.item.Entry

import android.icu.util.Currency
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.intern.calculator.goods.R
import com.intern.calculator.goods.data.Classes.QuantityUnit
import com.intern.calculator.goods.ui.AppViewModelProvider
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.util.Locale

object ItemEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    receivedVariable: Int,
    buttonText: Int,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var quantityUnitList = viewModel.getQuantityUnitList()
    if (quantityUnitList.isNotEmpty()) {
        Scaffold(
            topBar = {
                MyTopAppBar(
                    title = stringResource(ItemEntryDestination.titleRes),
                    navigationIcon = Icons.Outlined.ArrowBack,
                    navigationIconContentDescription = "Navigate back",
                    actionIcon = null,
                    actionIconContentDescription = null,
                    onNavigationClick = onNavigateUp
                )
            }
        ) { innerPadding ->
            ItemEntryBody(
                itemUiState = viewModel.itemUiState,
                quantityUnitList = quantityUnitList,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateUiState(
                            viewModel.itemUiState.itemDetails.copy(
                                aList = receivedVariable.toString(),
                                quantityType =
                                quantityUnitList.first {
                                    context.getString(it.name) ==
                                            viewModel.itemUiState.itemDetails.quantityType
                                }.id.toString()
                            )
                        )

                        viewModel.saveItem()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                buttonText = buttonText,
            )
        }
    } else {
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
fun ItemEntryBody(
    itemUiState: ItemUiState,
    quantityUnitList: List<QuantityUnit>,
    onItemValueChange: (ItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: Int,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(16.dp)
    ) {
        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            quantityUnitList = quantityUnitList,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(buttonText))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    quantityUnitList: List<QuantityUnit>,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true,
) {
    val context = LocalContext.current
    val options = quantityUnitList.map { context.getString(it.name) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember {
        mutableStateOf(
            if (itemDetails.quantityType.isNullOrBlank() and options.isNotEmpty()) {
                options[0]
            } else {
                options[itemDetails.quantityType.toInt() - 1]
            }
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            OutlinedTextField(
                value = itemDetails.name,
                onValueChange = {
                    onValueChange(
                        itemDetails.copy(name = it)
                    )
                },
                label = { Text(stringResource(R.string.item_entry_name_placeholder)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            OutlinedTextField(
                value = itemDetails.price,
                onValueChange = {
                    if (itemDetails.quantityType.isNullOrBlank()) {
                        onValueChange(
                            itemDetails.copy(
                                price = it,
                                quantityType = selectedOptionText
                            )
                        )
                    } else {
                        onValueChange(
                            itemDetails.copy(
                                price = it
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.item_entry_price_placeholder)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Column(
                modifier = modifier.weight(2f)
            ) {
                OutlinedTextField(
                    value = itemDetails.quantity,
                    onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.item_entry_quantity_placeholder)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
            }
            Column(
                modifier = modifier.weight(1f)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                )
                {
                    OutlinedTextField(
                        readOnly = true,
                        value =
                        if (itemDetails.quantityType.isNullOrBlank()) {
                            selectedOptionText.substringBefore(" ")
                        } else {
                            context.getString(
                                quantityUnitList.firstOrNull() {
                                    context.getString(it.name) ==
                                            itemDetails.quantityType
                                }?.name ?: quantityUnitList.first {
                                    (it.id) == itemDetails.quantityType.toInt()
                                }.name
                            )
                        },
                        onValueChange = {},
                        label = { Text(text = stringResource(id = R.string.quantity_type)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor(),
                        singleLine = true,
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(text = selectionOption) },
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false

                                    onValueChange(
                                        itemDetails.copy(
                                            quantityType = selectionOption
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}