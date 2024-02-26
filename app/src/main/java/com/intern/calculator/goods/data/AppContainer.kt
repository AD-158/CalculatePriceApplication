package com.intern.calculator.goods.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.intern.calculator.goods.data.repository.offline.OfflineCategoryRepository
import com.intern.calculator.goods.data.repository.offline.OfflineItemsRepository
import com.intern.calculator.goods.data.repository.offline.OfflineQuantityUnitRepository
import com.intern.calculator.goods.data.repository.online.CategoryRepository
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import com.intern.calculator.goods.data.repository.online.QuantityUnitRepository
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
 * [AppContainer] implementation that provides instances of [OfflineItemsRepository], [OfflineCategoryRepository], [OfflineQuantityUnitRepository],
 * and [UserPreferencesRepository].
 */
class AppDataContainer(private val context: Context) : AppContainer {
    // DataStore for storing user preferences
    private val Context.dataStore by preferencesDataStore(
        name = "settings"
    )

    // Lazily initialize the repositories with database instances
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
        UserPreferencesRepository(dataStore = context.dataStore)
    }
}