package com.intern.calculator.goods.data.Repository.Online

import com.intern.calculator.goods.data.Classes.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesStream(): Flow<List<Category>>

    fun getCategoryStream(name: String): Flow<Category?>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)
}