package com.intern.calculator.goods.data.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "t_item",
    foreignKeys = [
        // Define a foreign key relationship with the Category entity
        ForeignKey(
            entity = Category::class,
            parentColumns = ["t_category_id"],
            childColumns = ["t_item_category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        // Define a foreign key relationship with the QuantityUnit entity
        ForeignKey(
            entity = QuantityUnit::class,
            parentColumns = ["t_quantity_unit_id"],
            childColumns = ["t_item_quantity_type"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "t_item_id") val id: Int = 0,
    @ColumnInfo(name = "t_item_name", defaultValue = "Товар") val name: String,
    @ColumnInfo(name = "t_item_price", defaultValue = "0.0") val price: Double,
    @ColumnInfo(name = "t_item_quantity", defaultValue = "0.0") val quantity: Double,
    @ColumnInfo(name = "t_item_category_id", defaultValue = "0") val aList: Int,
    @ColumnInfo(name = "t_item_quantity_type", defaultValue = "0") val quantityType: Int,
)