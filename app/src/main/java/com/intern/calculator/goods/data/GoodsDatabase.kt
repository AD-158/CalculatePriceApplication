package com.intern.calculator.goods.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.intern.calculator.goods.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Item::class, Category::class, QuantityUnit::class], version = 1, exportSchema = false)
abstract class GoodsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDAO
    abstract fun categoryDao(): CategoryDAO

    abstract fun quantityUnitDao(): QuantityUnitDAO

    companion object {
        @Volatile
        private var Instance: GoodsDatabase? = null
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

    private class DatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Launch a coroutine to perform database operations
            CoroutineScope(Dispatchers.IO).launch {
                // Obtain the categoryDao
                val categoryDao = getDatabase(context).categoryDao()

                // Example: Inserting a category when the database is created
                val category = Category(name = context.getString(R.string.first_list))
                categoryDao.insert(category)
                val quantityUnitDAO = getDatabase(context).quantityUnitDao()
                quantityUnitDAO.insert(QuantityUnit(name = R.string.g, multiplier = 1000))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.kg, multiplier = 1))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.ml, multiplier = 1000))
                quantityUnitDAO.insert(QuantityUnit(name = R.string.l, multiplier = 1))
            }
        }
    }

}