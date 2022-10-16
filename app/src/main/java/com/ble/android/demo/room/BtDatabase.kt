package com.ble.android.demo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ble.android.demo.model.BtApiData

@Database(entities = [BtApiData::class], version = 1, exportSchema = false)
abstract class BtDatabase : RoomDatabase() {

    abstract fun getBtDataDao(): BtDataDao

    companion object {
        private val DB_NAME = "ble_raw_database.db"
        @Volatile private var instance: BtDatabase? = null

        fun getInstance(context: Context) : BtDatabase {
            return instance ?: synchronized(this) {
                val obj = Room.databaseBuilder(
                    context.applicationContext,
                    BtDatabase::class.java,
                    DB_NAME
                ).build()
                instance = obj
                return instance!!
            }
        }
    }
}