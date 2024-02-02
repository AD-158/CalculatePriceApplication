package com.intern.calculator.goods.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.intern.calculator.goods.CalculatePriceApplication
import com.intern.calculator.goods.ui.home.HomeViewModel
import com.intern.calculator.goods.ui.item.ItemDetailsViewModel
import com.intern.calculator.goods.ui.item.ItemEditViewModel
import com.intern.calculator.goods.ui.item.ItemEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                CalculatePriceApplication().container.itemsRepository,
                CalculatePriceApplication().container.quantityRepository,
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
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CalculatePriceApplication].
 */
fun CreationExtras.CalculatePriceApplication(): CalculatePriceApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CalculatePriceApplication)