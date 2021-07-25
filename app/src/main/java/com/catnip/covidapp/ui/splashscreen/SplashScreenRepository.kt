package com.catnip.covidapp.ui.splashscreen

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
class SplashScreenRepository(private val authDataSource: AuthDataSource) {

    suspend fun getAuthData(): BaseAuthResponse<UserResponse, String> =
        authDataSource.getAuthData()

}