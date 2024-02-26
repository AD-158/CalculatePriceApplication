package com.intern.calculator.goods.ui.item.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.data.repository.online.QuantityUnitRepository
import com.intern.calculator.goods.ui.home.QuantityUnitUiState
import com.intern.calculator.goods.ui.item.entry.ItemDetails
import com.intern.calculator.goods.ui.item.entry.toItem
import com.intern.calculator.goods.ui.item.entry.toItemDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// ViewModel for displaying item details
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    quantityUnitRepository: QuantityUnitRepository,
) : ViewModel() {

    // ID of the item to display details for
    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    // State flow representing the UI state of the item details
    val uiState: StateFlow<ItemDetailsUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                ItemDetailsUiState(itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState()
            )

    // State flow representing the UI state of quantity units
    private val quantityUnitUiState: StateFlow<QuantityUnitUiState> =
        quantityUnitRepository.getAllQuantityUnitStream().map { QuantityUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = QuantityUnitUiState()
            )

    // Function to delete the item
    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
    }

    // Composable function to get the list of quantity units
    @Composable
    fun getQuantityUnitList(): List<QuantityUnit> {
        return quantityUnitUiState.collectAsState().value.quantityUnitList
    }

    // Companion object holding constant values
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

// UI state data class for item details
data class ItemDetailsUiState(
    val itemDetails: ItemDetails = ItemDetails()
)