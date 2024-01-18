package com.intern.calculator.goods.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "t_item",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("t_category_id"),
        childColumns = arrayOf("t_item_category_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "t_item_id") val id: Int = 0,
    @ColumnInfo(name = "t_item_name", defaultValue = "Товар") val name: String,
    @ColumnInfo(name = "t_item_price", defaultValue = "0.0") val price: Double,
    @ColumnInfo(name = "t_item_weight", defaultValue = "0.0") val weight: Double,
    @ColumnInfo(name = "t_item_category_id", defaultValue = "0") val aList: Int,
)