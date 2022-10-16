package com.ble.android.demo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ble.android.demo.model.BtApiData

@Dao
interface BtDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRawData(btApiData: BtApiData)
}