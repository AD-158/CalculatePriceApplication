package com.intern.calculator.goods.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.Classes.Category
import com.intern.calculator.goods.data.Repository.Online.CategoryRepository
import com.intern.calculator.goods.data.Classes.Item
import com.intern.calculator.goods.data.Repository.Online.ItemsRepository
import com.intern.calculator.goods.data.Classes.QuantityUnit
import com.intern.calculator.goods.data.Repository.Online.QuantityUnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val itemsRepository: ItemsRepository,
    quantityUnitRepository: QuantityUnitRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val quantityUnitUiState: StateFlow<QuantityUnitUiState> =
        quantityUnitRepository.getAllQuantityUnitStream().map { QuantityUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = QuantityUnitUiState()
            )

    private val categoryUiState: StateFlow<CategoryUiState> =
        categoryRepository.getAllCategoriesStream().map { CategoryUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CategoryUiState()
            )

    // MutableStateFlow to hold the UI state
    private val _homeUiState = MutableStateFlow(HomeUiState())
    private val _ready = MutableStateFlow(false)

    // Public StateFlow that can be observed by the UI
    val homeUiState: MutableStateFlow<HomeUiState> get() = _homeUiState
    val ready: MutableStateFlow<Boolean> get() = _ready

    // Function to update HomeUiState itemList based on the result of getAllItemsForListSteam
    fun updateItemListBasedOnId(id: Int) {
        viewModelScope.launch {
            // Use the getAllItemsForListSteam function from the repository
            itemsRepository.getAllItemsForListSteam(id).collect { newList ->
                // Update the HomeUiState with the new list of items
                val newUiState = HomeUiState(newList)
                _homeUiState.value = newUiState
                _ready.value = true
            }
        }
    }

    @Composable
    fun getQuantityUnitList(): List<QuantityUnit> {
        return quantityUnitUiState.collectAsState().value.quantityUnitList
    }

    @Composable
    fun getCategoryList(): List<Category> {
        return categoryUiState.collectAsState().value.categoryList
    }

    suspend fun createCategory(category: Category) {
        categoryRepository.insertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryRepository.deleteCategory(category)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(var itemList: List<Item> = listOf())
data class QuantityUnitUiState(var quantityUnitList: List<QuantityUnit> = listOf())
data class CategoryUiState(var categoryList: List<Category> = listOf())