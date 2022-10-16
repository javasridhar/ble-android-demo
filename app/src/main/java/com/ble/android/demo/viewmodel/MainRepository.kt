package com.ble.android.demo.viewmodel

import com.ble.android.demo.api.ApiInterface
import com.ble.android.demo.model.BtApiData
import com.ble.android.demo.room.BtDatabase

class MainRepository(private val apiInterface: ApiInterface, private val btDatabase: BtDatabase) {
    suspend fun postRawData(rawData: BtApiData) = apiInterface.postBtRawData(rawData)
    suspend fun insertRawData(btApiData: BtApiData) = btDatabase.getBtDataDao().insertRawData(btApiData)
}