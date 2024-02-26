package com.intern.calculator.goods.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.intern.calculator.goods.data.classes.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDAO {
    // Insert an item into the database
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Item)

    // Update an item in the database
    @Update
    suspend fun update(item: Item)

    // Delete an item from the database
    @Delete
    suspend fun delete(item: Item)

    // Get all items for a given category ID from the database
    @Query("SELECT * from t_item WHERE t_item_category_id = :id")
    fun getAllItemsForList(id: Int): Flow<List<Item>>

    // Get all items from the database, ordered by item name in ascending order
    @Query("SELECT * from t_item ORDER BY t_item_name ASC")
    fun getAllItems(): Flow<List<Item>>

    // Get an item from the database by its ID
    @Query("SELECT * from t_item WHERE t_item_id = :id")
    fun getItem(id: Int): Flow<Item>
}