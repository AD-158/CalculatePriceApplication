package com.intern.calculator.goods.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class, Category::class], version = 1, exportSchema = false)
abstract class GoodsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDAO
    abstract fun categoryDao(): CategoryDAO

    companion object {
        @Volatile
        private var Instance : GoodsDatabase? = null

        fun getDatabase(context: Context) : GoodsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GoodsDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}