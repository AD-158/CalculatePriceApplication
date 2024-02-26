package com.intern.calculator.goods.ui.item.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.data.repository.online.QuantityUnitRepository
import com.intern.calculator.goods.ui.home.QuantityUnitUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.NumberFormat

// ViewModel responsible for handling the logic related to adding new items
class ItemEntryViewModel(
    private val itemsRepository: ItemsRepository,
    quantityUnitRepository: QuantityUnitRepository
) : ViewModel() {
    // Mutable state to hold the UI state of the item entry screen
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    // StateFlow to represent the UI state of quantity units
    private val quantityUnitUiState: StateFlow<QuantityUnitUiState> =
        quantityUnitRepository.getAllQuantityUnitStream().map { QuantityUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = QuantityUnitUiState()
            )

    // Update the UI state based on the entered item details
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    // Save the item to the repository if input is valid
    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    // Validate the input fields
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
//            name.isNotBlank() &&
            price.isNotBlank()
                    && quantity.isNotBlank()
        }
    }

    // Function to get the list of quantity units, collected as state in Composable
    @Composable
    fun getQuantityUnitList(): List<QuantityUnit> {
        return quantityUnitUiState.collectAsState().value.quantityUnitList
    }

    // Companion object holding constant values
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

// UI state for the item entry screen
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

// Data class representing the details of an item
data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val aList: String = "",
    var quantityType: String = "",
)

// Extension function to convert ItemDetails to Item
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.replace(",",".").toDoubleOrNull() ?: 0.0,
    quantity = quantity.toDoubleOrNull() ?: 0.0,
    aList = aList.toIntOrNull() ?: 0,
    quantityType = quantityType.toIntOrNull() ?: 1,
)

// Extension function to format the price of an item
fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

// Extension function to convert Item to ItemUiState
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

// Extension function to convert Item to ItemDetails
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    aList = aList.toString(),
    quantityType = quantityType.toString()
)