package com.catnip.covidapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.entity.request.authentification.LoginRequest
import com.catnip.covidapp.data.network.entity.responses.authentication.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class LoginViewModel(private val loginRepository: LoginRepository) :
    ViewModel() {
    val loginData = MutableLiveData<Resource<LoginResponse>>()

    fun postLoginData(loginRequest: LoginRequest) {
        loginData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginRepository.postLoginData(loginRequest)
                viewModelScope.launch(Dispatchers.Main) {
                    if (response.success) {
                        loginData.value = Resource.Success(response.data)
                    } else {
                        loginData.value = Resource.Error(response.errors)
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    loginData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }
}