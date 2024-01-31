package com.intern.calculator.goods.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.intern.calculator.goods.data.Item
import com.intern.calculator.goods.data.ItemsRepository
import java.text.NumberFormat
import com.intern.calculator.goods.data.QuantityUnit
import com.intern.calculator.goods.data.asEnumOrDefault

// ViewModel для проверки и вставки item в базу данных Room.
class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    // Сохраняет текущее состояние пользовательского интерфейса item
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Обновляет [itemUiState] значением, указанным в аргументе. Этот метод также запускает
     * проверку для входных значений.
     */
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

// Представляет состояние пользовательского интерфейса для item.
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

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toDoubleOrNull() ?: 0.0,
    aList = aList.toIntOrNull() ?: 0,
    quantityType = quantityType.asEnumOrDefault<QuantityUnit>() ?: QuantityUnit.g
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Конвертация [Item] в [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Конвертация [Item] а [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    aList = aList.toString(),
    quantityType = quantityType.toString()
)