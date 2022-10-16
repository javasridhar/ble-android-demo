package com.ble.android.demo.api

import com.ble.android.demo.model.BtApiData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiInterface {

    @POST(value = "/api/postBleRawData")
    suspend fun postBtRawData(@Body rawData: BtApiData) : Response<BtApiData>

    companion object {
        private const val BASE_URL = "https://ble-demo-server.herokuapp.com"

        var apiInterface: ApiInterface? = null
        var client = OkHttpClient.Builder()

        private fun getOkHttpClient() : OkHttpClient {
            client.connectTimeout(1, TimeUnit.MINUTES)
            client.readTimeout(1, TimeUnit.MINUTES)
            client.writeTimeout(1, TimeUnit.MINUTES)
            client.callTimeout(1, TimeUnit.MINUTES)

            return client.build()
        }

        fun getInstance() : ApiInterface {
            if (apiInterface == null) {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                apiInterface = retrofit.create(ApiInterface::class.java)
            }
            return apiInterface!!
        }
    }
}