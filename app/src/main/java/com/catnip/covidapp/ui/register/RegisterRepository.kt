package com.catnip.covidapp.ui.register

import com.catnip.covidapp.data.network.datasource.AuthDataSource
import com.catnip.covidapp.data.network.entity.request.authentification.LoginRequest
import com.catnip.covidapp.data.network.entity.request.authentification.RegisterRequest
import com.catnip.covidapp.data.network.entity.responses.authentication.BaseAuthResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.LoginResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.UserResponse

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class RegisterRepository(private val authDataSource: AuthDataSource) {

    suspend fun postRegisterData(registerRequest: RegisterRequest): BaseAuthResponse<UserResponse, String> =
        authDataSource.postRegisterData(registerRequest)

}