package com.intern.calculator.goods.ui.settings

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.intern.calculator.goods.data.classes.Category
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.repository.online.CategoryRepository
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


// Define the SettingsViewModel class which extends ViewModel
class SettingsViewModel(
    private val repository: UserPreferencesRepository,
    categoryRepository: CategoryRepository,
    itemRepository: ItemsRepository,
) : ViewModel() {

    // Flow to observe user preferences
    val userPreferences: Flow<UserPreferences> = repository.userPreferencesFlow
    val categories: Flow<List<Category>> = categoryRepository.getAllCategoriesStream()
    val items: Flow<List<Item>> = itemRepository.getAllItemsStream()

    // Function to update the theme
    suspend fun updateTheme(theme: Theme) {
        repository.updateTheme(theme.name)
    }

    // Function to update the language
    suspend fun updateLanguage(language: Language) {
        repository.updateLanguage(language.name)

        // Set application locale based on selected language
        val locale = when (language) {
            Language.English -> "en"
            Language.Russian -> "ru"
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
    }

    // Function to convert locale string to Language enum
    fun toLanguage(locale: String): Language {
        return when (locale) {
            "en" -> Language.English
            "ru" -> Language.Russian
            else -> Language.English
        }
    }

    // Function to update the selected list number
    suspend fun updateSelectedList(listNumber: Int) {
        repository.updateSelectedList(listNumber)
    }

    // Function to update the duration
    suspend fun updateDuration(duration: Long) {
        repository.updateDuration(duration)
    }

    // Function to get all data from db and put it into json
    fun prepareDataForExport(
        categories: List<Category>,
        items: List<Item>,
    ): String {
        // Convert data to JSON
        val jsonCategories = convertCategoriesToJson(categories)
        val jsonItems = convertItemsToJson(items)

        val jsonData = JSONObject()
        jsonData.put("categories", jsonCategories)
        jsonData.put("items", jsonItems)

        return jsonData.toString()
    }

    // Function to put json into the file
    suspend fun putJsonToFile(
        context: Context,
        uri: Uri,
        jsonData: String
    ) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonData.toByteArray())
            }
        }
    }

    // Function to convert Category data to json
    private fun convertCategoriesToJson(categories: List<Category>): JSONArray {
        val jsonArray = JSONArray()
        categories.forEach { category ->
            val jsonObject = JSONObject()
            jsonObject.put("id", category.id)
            jsonObject.put("name", category.name)
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }

    // Function to convert Item data to json
    private fun convertItemsToJson(items: List<Item>): JSONArray {
        val jsonArray = JSONArray()
        items.forEach { item ->
            val jsonObject = JSONObject()
            jsonObject.put("id", item.id)
            jsonObject.put("name", item.name)
            jsonObject.put("price", item.price)
            jsonObject.put("quantity", item.quantity)
            jsonObject.put("categoryId", item.aList)
            jsonObject.put("quantityTypeId", item.quantityType)
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }
}
