package com.intern.calculator.goods.data

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.intern.calculator.goods.ui.settings.UserPreferencesRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val categoryRepository: CategoryRepository
    val quantityRepository: QuantityUnitRepository
    val settingsRepository: UserPreferencesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository] and [OfflineCategoryRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    private val Context.dataStore by preferencesDataStore(
        name = "settings"
    )
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(GoodsDatabase.getDatabase(context).itemDao())
    }
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(GoodsDatabase.getDatabase(context).categoryDao())
    }
    override val quantityRepository: QuantityUnitRepository by lazy {
        OfflineQuantityUnitRepository(GoodsDatabase.getDatabase(context).quantityUnitDao())
    }
    override val settingsRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore = context.dataStore, context= context)
    }

}