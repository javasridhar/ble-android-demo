package com.ble.android.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ble.android.demo.model.BtApiData
import kotlinx.coroutines.*

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val btApiData = MutableLiveData<BtApiData>()
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorMessage.value = "Exception on=> ${throwable.localizedMessage}"
    }

    fun postRawData(rawData: BtApiData) {
        job = CoroutineScope(Dispatchers.IO).launch {
                val response = mainRepository.postRawData(rawData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        btApiData.value = response.body()
                    } else {
                        errorMessage.value = "Network Error on=> ${response.code()}, ${response.message()}"
                    }
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}