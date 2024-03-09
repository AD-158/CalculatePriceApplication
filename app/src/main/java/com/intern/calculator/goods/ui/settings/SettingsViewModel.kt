package com.intern.calculator.goods.ui.settings

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intern.calculator.goods.data.classes.Category
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.repository.online.CategoryRepository
import com.intern.calculator.goods.data.repository.online.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


// Define the SettingsViewModel class which extends ViewModel
class SettingsViewModel(
    private val repository: UserPreferencesRepository,
    val categoryRepository: CategoryRepository,
    val itemRepository: ItemsRepository,
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

    // Function to import data from json
    suspend fun importDataFromJson(
        context: Context,
        uri: Uri
    ) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val jsonData = inputStream?.bufferedReader().use { it?.readText() }
        val map: MutableMap<Int, Int> = mutableMapOf()

        jsonData?.let {
            val jsonObject = JSONObject(it)
            val categoriesArray = jsonObject.getJSONArray("categories")
            val itemsArray = jsonObject.getJSONArray("items")
            viewModelScope.launch {
                for (i in 0 until categoriesArray.length()) {
                    val categoryObject = categoriesArray.getJSONObject(i)
                    val name = categoryObject.getString("name")
                    val oldCategoryId = categoryObject.getInt("id")
                    categoryRepository.insertCategory(Category(name = name))
                    val newCategoryId = categoryRepository.getCategory(name)?.id ?: 1
                    map[oldCategoryId] = newCategoryId
                }

                for (i in 0 until itemsArray.length()) {
                    val itemObject = itemsArray.getJSONObject(i)
                    val name = itemObject.getString("name")
                    val price = itemObject.getDouble("price")
                    val quantity = itemObject.getDouble("quantity")
                    val oldCategoryId = itemObject.getInt("categoryId")
                    val newCategoryId = map[oldCategoryId] ?: 1
                    val quantityTypeId = itemObject.getInt("quantityTypeId")

                    val item = Item(
                        name = name,
                        price = price,
                        quantity = quantity,
                        aList = newCategoryId,
                        quantityType = quantityTypeId
                    )
                    itemRepository.insertItem(item)
                }
            }
        }
    }
}
