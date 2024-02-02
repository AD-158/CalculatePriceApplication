package com.intern.calculator.goods.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_quantity_unit")
data class QuantityUnit(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "t_quantity_unit_id") val id: Int = 0,
    @ColumnInfo(name = "t_quantity_unit_name") val name: Int,
    @ColumnInfo(name = "t_quantity_unit_multiplier", defaultValue = "1") val multiplier: Int,
)
