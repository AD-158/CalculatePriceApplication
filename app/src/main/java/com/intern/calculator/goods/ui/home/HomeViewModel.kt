package com.intern.calculator.goods.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.Item
import com.intern.calculator.goods.data.ItemsRepository
import com.intern.calculator.goods.data.QuantityUnit
import com.intern.calculator.goods.data.QuantityUnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val itemsRepository: ItemsRepository,
    private val quantityUnitRepository: QuantityUnitRepository
) : ViewModel() {

    private val quantityUnitUiState: StateFlow<QuantityUnitUiState> =
        quantityUnitRepository.getAllQuantityUnitStream().map { QuantityUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = QuantityUnitUiState()
            )

    // MutableStateFlow to hold the UI state
    private val _homeUiState = MutableStateFlow(HomeUiState())

    // Public StateFlow that can be observed by the UI
    val homeUiState: MutableStateFlow<HomeUiState> get() = _homeUiState


    // Function to update HomeUiState itemList based on the result of getAllItemsForListSteam
    fun updateItemListBasedOnId(id: Int) {
        viewModelScope.launch {
            // Use the getAllItemsForListSteam function from the repository
            itemsRepository.getAllItemsForListSteam(id).collect { newList ->
                // Update the HomeUiState with the new list of items
                val newUiState = HomeUiState(newList)
                _homeUiState.value = newUiState
            }
        }
    }

    @Composable
    fun getQuantityUnitList(): List<QuantityUnit> {
        return quantityUnitUiState.collectAsState().value.quantityUnitList
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(var itemList: List<Item> = listOf())
data class QuantityUnitUiState(var quantityUnitList: List<QuantityUnit> = listOf())