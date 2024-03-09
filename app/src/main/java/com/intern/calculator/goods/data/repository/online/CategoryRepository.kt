package com.intern.calculator.goods.data.repository.online

import com.intern.calculator.goods.data.classes.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesStream(): Flow<List<Category>>

    suspend fun getCategory(name: String): Category?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun resetAutoIncrement(tableName: String?)
}