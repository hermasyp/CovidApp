package com.catnip.covidapp.ui.splashscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.entity.responses.authentication.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class SplashScreenViewModel(private val splashScreenRepository: SplashScreenRepository) :
    ViewModel() {
    val syncData = MutableLiveData<Resource<UserResponse>>()

    fun fetchSyncData() {
        syncData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = splashScreenRepository.getAuthData()
                viewModelScope.launch(Dispatchers.Main) {
                    if (response.success) {
                        syncData.value = Resource.Success(response.data)
                    } else {
                        syncData.value = Resource.Error(response.errors)
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    syncData.value = Resource.Error(e.message.orEmpty())
                }
            }

        }
    }

}