package com.intern.calculator.goods.data.repository.offline

import com.intern.calculator.goods.data.classes.Category
import com.intern.calculator.goods.data.dao.CategoryDAO
import com.intern.calculator.goods.data.repository.online.CategoryRepository
import kotlinx.coroutines.flow.Flow

class OfflineCategoryRepository(private val categoryDao: CategoryDAO) : CategoryRepository {
    // Get a flow of all categories from the local database
    override fun getAllCategoriesStream(): Flow<List<Category>> = categoryDao.getAllCategories()

    // Get a flow of a specific category by its name from the local database
    override fun getCategoryStream(name: String): Flow<Category?> = categoryDao.getCategory(name)

    // Insert a new category into the local database
    override suspend fun insertCategory(category: Category) = categoryDao.insert(category)

    // Delete a category from the local database
    override suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    // Update an existing category in the local database
    override suspend fun updateCategory(category: Category) = categoryDao.update(category)

    // Reset the auto-increment value for the given table in the local database
    override suspend fun resetAutoIncrement(tableName: String?) = categoryDao.resetAutoIncrement(tableName)
}