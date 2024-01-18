package com.intern.calculator.goods.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * from t_category WHERE t_category_id = :id")
    fun getCategory(id: Int): Flow<Category>

    @Query("SELECT * from t_category ORDER BY t_category_name ASC")
    fun getAllCategories(): Flow<List<Category>>
}