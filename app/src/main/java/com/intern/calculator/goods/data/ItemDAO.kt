package com.intern.calculator.goods.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from t_item WHERE t_item_category_id = :id")
    fun getAllItemsForList(id: Int): Flow<List<Item>>

    @Query("SELECT * from t_item ORDER BY t_item_name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * from t_item WHERE t_item_id = :id")
    fun getItem(id: Int): Flow<Item>
}