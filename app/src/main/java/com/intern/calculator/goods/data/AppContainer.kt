package com.intern.calculator.goods.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val categoryRepository: CategoryRepository
    val quantityRepository: QuantityUnitRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository] and [OfflineCategoryRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(GoodsDatabase.getDatabase(context).itemDao())
    }
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(GoodsDatabase.getDatabase(context).categoryDao())
    }
    override val quantityRepository: QuantityUnitRepository by lazy {
        OfflineQuantityUnitRepository(GoodsDatabase.getDatabase(context).quantityUnitDao())
    }
}