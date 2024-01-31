package com.intern.calculator.goods.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.Item
import com.intern.calculator.goods.data.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(val itemsRepository: ItemsRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [ItemsRepository] and mapped to
     * [HomeUiState]
     */
//    val homeUiState: StateFlow<HomeUiState> =
//        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = HomeUiState()
//            )

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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(var itemList: List<Item> = listOf())