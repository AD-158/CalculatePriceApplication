package com.intern.calculator.goods.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.intern.calculator.goods.data.classes.QuantityUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface QuantityUnitDAO {
    // Insert a quantity unit into the database
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(quantityUnit: QuantityUnit)

    // Update a quantity unit in the database
    @Update
    suspend fun update(quantityUnit: QuantityUnit)

    // Delete a quantity unit from the database
    @Delete
    suspend fun delete(quantityUnit: QuantityUnit)

    // Get all quantity units from the database
    @Query("SELECT * FROM t_quantity_unit")
    fun getAllQuantityUnit(): Flow<List<QuantityUnit>>

    // Get a specific quantity unit from the database by its name
    @Query("SELECT * FROM t_quantity_unit WHERE t_quantity_unit_name = :name LIMIT 1")
    fun getSpecificQuantityUnit(name: Int): Flow<QuantityUnit>
}