package com.intern.calculator.goods.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.intern.calculator.goods.data.Classes.QuantityUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface QuantityUnitDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(quantityUnit: QuantityUnit)

    @Update
    suspend fun update(quantityUnit: QuantityUnit)

    @Delete
    suspend fun delete(quantityUnit: QuantityUnit)

    @Query("SELECT * FROM t_quantity_unit")
    fun getAllQuantityUnit(): Flow<List<QuantityUnit>>

    @Query("SELECT * FROM t_quantity_unit WHERE t_quantity_unit_name = :name LIMIT 1")
    fun getSpecificQuantityUnit(name: Int): Flow<QuantityUnit>
}