package com.intern.calculator.goods.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.intern.calculator.goods.data.classes.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    // Insert a category into the database
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    // Update a category in the database
    @Update
    suspend fun update(category: Category)

    // Delete a category from the database
    @Delete
    suspend fun delete(category: Category)

    // Get a category from the database by its name
    @Query("SELECT * from t_category WHERE t_category_name = :name ORDER BY t_category_id DESC")
    suspend fun getCategory(name: String): Category?

    // Get all categories from the database, ordered by category ID in ascending order
    @Query("SELECT * from t_category ORDER BY t_category_id ASC")
    fun getAllCategories(): Flow<List<Category>>

    // Reset the auto-increment value for a given table
    @Query("DELETE FROM sqlite_sequence WHERE name = :tableName")
    fun resetAutoIncrement(tableName: String?)
}