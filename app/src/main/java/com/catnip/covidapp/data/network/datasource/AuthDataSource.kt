package com.catnip.covidapp.data.network.datasource

import com.catnip.covidapp.data.network.entity.request.authentification.LoginRequest
import com.catnip.covidapp.data.network.entity.request.authentification.RegisterRequest
import com.catnip.covidapp.data.network.entity.responses.authentication.BaseAuthResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.LoginResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.UserResponse
import com.catnip.covidapp.data.network.services.BinarAuthServices

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class AuthDataSource(private val authServices: BinarAuthServices) {

    suspend fun postRegisterData(registerRequest: RegisterRequest): BaseAuthResponse<UserResponse, String> =
        authServices.postRegisterData(registerRequest)

    suspend fun postLoginData(loginRequest: LoginRequest): BaseAuthResponse<LoginResponse, String> =
        authServices.postLoginData(loginRequest)

    suspend fun getAuthData(): BaseAuthResponse<UserResponse, String> =
        authServices.getAuthData()


}