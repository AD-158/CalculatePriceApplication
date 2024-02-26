package com.intern.calculator.goods.ui.item.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.data.repository.online.QuantityUnitRepository
import com.intern.calculator.goods.ui.home.QuantityUnitUiState
import com.intern.calculator.goods.ui.item.entry.ItemDetails
import com.intern.calculator.goods.ui.item.entry.ItemUiState
import com.intern.calculator.goods.ui.item.entry.toItem
import com.intern.calculator.goods.ui.item.entry.toItemUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    quantityUnitRepository: QuantityUnitRepository,
) : ViewModel() {

    // Mutable state for the UI state of the item being edited
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    // ID of the item being edited
    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    // Flow representing the UI state of quantity units
    private val quantityUnitUiState: StateFlow<QuantityUnitUiState> =
        quantityUnitRepository.getAllQuantityUnitStream().map { QuantityUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = QuantityUnitUiState()
            )

    init {
        // Fetch the item details and convert them to UI state when the ViewModel is initialized
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    // Function to update the item in the repository
    suspend fun updateItem() {
        if (validateInput(itemUiState.itemDetails)) {
            itemsRepository.updateItem(itemUiState.itemDetails.toItem())
        }
    }

    // Function to update the UI state based on item details
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    // Function to validate input for the item details
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
//            name.isNotBlank() &&
                    price.isNotBlank() && quantity.isNotBlank()
        }
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
