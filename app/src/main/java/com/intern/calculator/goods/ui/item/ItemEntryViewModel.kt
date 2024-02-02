package com.intern.calculator.goods.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.Item
import com.intern.calculator.goods.data.ItemsRepository
import com.intern.calculator.goods.data.QuantityUnit
import com.intern.calculator.goods.data.QuantityUnitRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat

class ItemEntryViewModel(
    private val itemsRepository: ItemsRepository,
    private val quantityUnitRepository: QuantityUnitRepository
) : ViewModel() {
    var itemUiState by mutableStateOf(ItemUiState())
        private set
    var quantityUnitUiStates:List<QuantityUnitUiState> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            quantityUnitRepository.getAllQuantityUnitStream()
                .collect { items ->
                    if (items.isNotEmpty()) {
                        quantityUnitUiStates = items.map { it.toQuantityUnitUiState() }
                    }
                }
        }
    }

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
//            name.isNotBlank() &&
            price.isNotBlank()
                    && quantity.isNotBlank()
        }
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val aList: String = "",
    var quantityType: String = "",
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.replace(",",".").toDoubleOrNull() ?: 0.0,
    quantity = quantity.toDoubleOrNull() ?: 0.0,
    aList = aList.toIntOrNull() ?: 0,
    quantityType = quantityType.toIntOrNull() ?: 1,
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    aList = aList.toString(),
    quantityType = quantityType.toString()
)

data class QuantityUnitUiState(
    val quantityUnitDetails: QuantityUnitDetails = QuantityUnitDetails(),
)

data class QuantityUnitDetails(
    val id: Int = 0,
    val name: Int = 0,
    val multiplier: Int = 0,
)

fun QuantityUnit.toQuantityUnitUiState(): QuantityUnitUiState = QuantityUnitUiState(
    quantityUnitDetails = this.toQuantityUnitDetails(),
)

fun QuantityUnit.toQuantityUnitDetails(): QuantityUnitDetails = QuantityUnitDetails(
    id = id,
    name = name,
    multiplier = multiplier,
)