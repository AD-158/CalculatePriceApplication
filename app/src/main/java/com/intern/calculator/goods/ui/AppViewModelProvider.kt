package com.intern.calculator.goods.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.intern.calculator.goods.CalculatePriceApplication
import com.intern.calculator.goods.ui.home.HomeViewModel
import com.intern.calculator.goods.ui.item.Details.ItemDetailsViewModel
import com.intern.calculator.goods.ui.item.Edit.ItemEditViewModel
import com.intern.calculator.goods.ui.item.Entry.ItemEntryViewModel
import com.intern.calculator.goods.ui.settings.SettingsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                CalculatePriceApplication().container.itemsRepository,
                CalculatePriceApplication().container.quantityRepository,
                CalculatePriceApplication().container.categoryRepository,
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(
                CalculatePriceApplication().container.itemsRepository,
                CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                CalculatePriceApplication().container.itemsRepository,
                CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for ItemEditViewModel
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                CalculatePriceApplication().container.itemsRepository,
                CalculatePriceApplication().container.quantityRepository,
            )
        }
        // Initializer for SettingsViewModel
        initializer {
            SettingsViewModel(
                repository = CalculatePriceApplication().container.settingsRepository,
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