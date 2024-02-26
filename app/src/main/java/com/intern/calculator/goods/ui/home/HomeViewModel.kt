package com.intern.calculator.goods.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.classes.Category
import com.intern.calculator.goods.data.repository.online.CategoryRepository
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.data.repository.online.QuantityUnitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ViewModel for displaying home screen
class HomeViewModel(
    private val itemsRepository: ItemsRepository,
    quantityUnitRepository: QuantityUnitRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    // StateFlow for quantity unit UI state
    private val quantityUnitUiState: StateFlow<QuantityUnitUiState> =
        quantityUnitRepository.getAllQuantityUnitStream().map { QuantityUnitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = QuantityUnitUiState()
            )

    // StateFlow for category UI state
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

    // Function to update HomeUiState itemList based on the result of getAllItemsForListSteam
    fun updateItemListBasedOnId(id: Int) {
        viewModelScope.launch {
            itemsRepository.getAllItemsForListSteam(id).collect { newList ->
                // Update the HomeUiState with the new list of items
                val newUiState = HomeUiState(newList)
                _homeUiState.value = newUiState
                _ready.value = true
            }
        }
    }

    // Function to get the list of quantity units
    @Composable
    fun getQuantityUnitList(): List<QuantityUnit> {
        return quantityUnitUiState.collectAsState().value.quantityUnitList
    }

    // Function to get the list of categories
    @Composable
    fun getCategoryList(): List<Category> {
        return categoryUiState.collectAsState().value.categoryList
    }

    // Function to create a new category
    suspend fun createCategory(category: Category) {
        categoryRepository.insertCategory(category)
    }

    // Function to update an existing category
    suspend fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
    }

    // Function to delete a category
    suspend fun deleteCategory(category: Category) {
        categoryRepository.deleteCategory(category)
    }

    // Function to reset auto-increment for a given table
    suspend fun resetAutoIncrement(tableName: String?) {
        withContext(Dispatchers.IO) {
            categoryRepository.resetAutoIncrement(tableName)
        }
    }

    // Companion object holding constant values
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

// Data class representing the UI state for the home screen
data class HomeUiState(var itemList: List<Item> = listOf())

// Data class representing the UI state for quantity units
data class QuantityUnitUiState(var quantityUnitList: List<QuantityUnit> = listOf())

// Data class representing the UI state for categories
data class CategoryUiState(var categoryList: List<Category> = listOf())