package com.intern.calculator.goods.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.intern.calculator.goods.R
import com.intern.calculator.goods.data.classes.Category
import com.intern.calculator.goods.data.classes.Item
import com.intern.calculator.goods.data.classes.QuantityUnit
import com.intern.calculator.goods.data.dao.CategoryDAO
import com.intern.calculator.goods.data.dao.ItemDAO
import com.intern.calculator.goods.data.dao.QuantityUnitDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Define the Room database with entities and version number
@Database(entities = [Item::class, Category::class, QuantityUnit::class], version = 1, exportSchema = false)
abstract class GoodsDatabase : RoomDatabase() {
    // Define abstract functions to retrieve DAOs
    abstract fun itemDao(): ItemDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun quantityUnitDao(): QuantityUnitDAO

    companion object {
        @Volatile
        private var Instance: GoodsDatabase? = null

        // Get an instance of the database
        fun getDatabase(context: Context): GoodsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GoodsDatabase::class.java, "item_database")
                    .addCallback(DatabaseCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }
    }

    // Database callback to populate initial data
    private class DatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Launch a coroutine to perform database operations
            CoroutineScope(Dispatchers.IO).launch {
                // Obtain the DAOs
                val categoryDao = getDatabase(context).categoryDao()
                val quantityUnitDAO = getDatabase(context).quantityUnitDao()

                // Insert initial data into Category and QuantityUnit tables
                categoryDao.insert(Category(name = context.getString(R.string.first_list)))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.g, multiplier = 1000))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.kg, multiplier = 1))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.ml, multiplier = 1000))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.l, multiplier = 1))
            }
        }
    }
}
