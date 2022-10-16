package com.ble.android.demo.viewmodel

import com.ble.android.demo.api.ApiInterface
import com.ble.android.demo.model.BtApiData

class MainRepository(private val apiInterface: ApiInterface) {
    suspend fun postRawData(rawData: BtApiData) = apiInterface.postBtRawData(rawData)
}