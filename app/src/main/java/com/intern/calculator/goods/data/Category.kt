package com.intern.calculator.goods.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_category")
data class Category(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "t_category_id") val id: Int = 0,
    @ColumnInfo(name = "t_category_name", defaultValue = "Список") val name: String,
//    @Relation(parentColumn = "t_category_id", entityColumn = "t_item_category_id", entity = Item::class) val songs: List<Item>,
)
