package com.catnip.covidapp.ui.login

import com.catnip.covidapp.data.network.datasource.AuthDataSource
import com.catnip.covidapp.data.network.entity.request.authentification.LoginRequest
import com.catnip.covidapp.data.network.entity.responses.authentication.BaseAuthResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.LoginResponse

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class LoginRepository(private val authDataSource: AuthDataSource) {

    suspend fun postLoginData(loginRequest: LoginRequest): BaseAuthResponse<LoginResponse, String> =
        authDataSource.postLoginData(loginRequest)

}