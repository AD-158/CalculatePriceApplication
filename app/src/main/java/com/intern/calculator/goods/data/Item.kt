package com.intern.calculator.goods.data

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.intern.calculator.goods.R
import kotlin.reflect.KClass

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
    @ColumnInfo(name = "t_item_quantity", defaultValue = "0.0") val quantity: Double,
    @ColumnInfo(name = "t_item_category_id", defaultValue = "0") val aList: Int,
    @ColumnInfo(name = "t_item_quantity_type", defaultValue = "0") val quantityType: QuantityUnit,
)

enum class QuantityUnit(@StringRes val title: Int) {
    ml(R.string.ml),
    l(R.string.l),
    g(R.string.g),
    kg(R.string.kg);

    companion object {
        // Function to find the enum element index by string value
        fun getIndexByString(value: String): Int {
            return entries.toTypedArray().indexOfFirst { it.name.equals(value, ignoreCase = true) }
        }
        @Composable
        fun findByStringValue(stringValue: String): QuantityUnit? {
            return entries.firstOrNull {
                LocalContext.current.getString(it.title) == stringValue
            }
        }

        fun findByIntValue(intValue: Int): QuantityUnit? {
            return entries.firstOrNull {
                it.title == intValue
            }
        }

        @Composable
        fun getList ():List<String>{
            val values = QuantityUnit.entries
            var list = listOf<String>()
            for (value in values)
            {
                list = list + stringResource(id = value.title)
            }
            return list
        }
    }
}

inline fun <reified T : Enum<T>> String.asEnumOrDefault(defaultValue: T? = null): T? =
    enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) } ?: defaultValue

fun QuantityUnit.mapToStringResource() : Int {
    return when (this) {
        QuantityUnit.ml -> R.string.ml
        QuantityUnit.l -> R.string.l
        QuantityUnit.kg -> R.string.kg
        QuantityUnit.g -> R.string.g
    }
}
