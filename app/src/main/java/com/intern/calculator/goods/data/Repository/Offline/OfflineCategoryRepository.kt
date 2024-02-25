package com.intern.calculator.goods.data.Repository.Offline

import com.intern.calculator.goods.data.Classes.Category
import com.intern.calculator.goods.data.DAO.CategoryDAO
import com.intern.calculator.goods.data.Repository.Online.CategoryRepository
import kotlinx.coroutines.flow.Flow

class OfflineCategoryRepository(private val categoryDao: CategoryDAO) : CategoryRepository {
    override fun getAllCategoriesStream(): Flow<List<Category>> = categoryDao.getAllCategories()

    override fun getCategoryStream(name: String): Flow<Category?> = categoryDao.getCategory(name)

    override suspend fun insertCategory(category: Category) = categoryDao.insert(category)

    override suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    override suspend fun updateCategory(category: Category) = categoryDao.update(category)
}