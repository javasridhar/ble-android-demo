package com.ble.android.demo.api

import com.ble.android.demo.model.BtApiData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST(value = "/api/postBleRawData")
    suspend fun postBtRawData(@Body rawData: BtApiData) : Response<BtApiData>

    companion object {
        private const val BASE_URL = "https://ble-demo-server.herokuapp.com"

        var apiInterface: ApiInterface? = null
        fun getInstance() : ApiInterface {
            if (apiInterface == null) {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                apiInterface = retrofit.create(ApiInterface::class.java)
            }
            return apiInterface!!
        }
    }
}