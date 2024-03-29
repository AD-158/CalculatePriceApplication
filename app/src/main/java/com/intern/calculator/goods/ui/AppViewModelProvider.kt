package com.intern.calculator.goods.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.intern.calculator.goods.CalculatePriceApplication
import com.intern.calculator.goods.ui.home.HomeViewModel
import com.intern.calculator.goods.ui.item.details.ItemDetailsViewModel
import com.intern.calculator.goods.ui.item.edit.ItemEditViewModel
import com.intern.calculator.goods.ui.item.entry.ItemEntryViewModel
import com.intern.calculator.goods.ui.settings.SettingsViewModel

// Object to provide view model instances using ViewModelFactory
object AppViewModelProvider {
    // Factory instance for creating view models
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                itemsRepository = CalculatePriceApplication().container.itemsRepository,
                categoryRepository = CalculatePriceApplication().container.categoryRepository,
                quantityUnitRepository = CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(
                itemsRepository = CalculatePriceApplication().container.itemsRepository,
                quantityUnitRepository = CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                itemsRepository = CalculatePriceApplication().container.itemsRepository,
                quantityUnitRepository = CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for ItemEditViewModel
        initializer {
            ItemEditViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                itemsRepository = CalculatePriceApplication().container.itemsRepository,
                quantityUnitRepository = CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for SettingsViewModel
        initializer {
            SettingsViewModel(
                repository = CalculatePriceApplication().container.settingsRepository,
                categoryRepository = CalculatePriceApplication().container.categoryRepository,
                itemRepository = CalculatePriceApplication().container.itemsRepository,
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CalculatePriceApplication].
 */
fun CreationExtras.CalculatePriceApplication(): CalculatePriceApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CalculatePriceApplication)