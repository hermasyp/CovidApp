package com.catnip.covidapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.entity.request.authentification.RegisterRequest
import com.catnip.covidapp.data.network.entity.responses.authentication.UserResponse
import com.catnip.covidapp.ui.splashscreen.SplashScreenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class RegisterViewModel(private val registerRepository: RegisterRepository) :
    ViewModel() {
    val registerData = MutableLiveData<Resource<UserResponse>>()

    fun postRegisterData(registerRequest: RegisterRequest) {
        registerData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = registerRepository.postRegisterData(registerRequest)
                viewModelScope.launch(Dispatchers.Main) {
                    if (response.success) {
                        registerData.value = Resource.Success(response.data)
                    } else {
                        registerData.value = Resource.Error(response.errors)
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    registerData.value = Resource.Error(e.message.orEmpty())
                }
            }

        }
    }
}