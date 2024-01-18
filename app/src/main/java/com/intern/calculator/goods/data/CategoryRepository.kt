package com.intern.calculator.goods.data

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategoriesStream(): Flow<List<Category>>

    fun getCategoryStream(id: Int): Flow<Category?>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)
}