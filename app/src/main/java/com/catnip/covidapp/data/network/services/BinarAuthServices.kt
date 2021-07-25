package com.catnip.covidapp.data.network.services

import android.content.Context
import com.catnip.covidapp.BuildConfig
import com.catnip.covidapp.data.local.sharedpreference.SessionPreference
import com.catnip.covidapp.data.network.entity.request.authentification.LoginRequest
import com.catnip.covidapp.data.network.entity.request.authentification.RegisterRequest
import com.catnip.covidapp.data.network.entity.responses.authentication.BaseAuthResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.LoginResponse
import com.catnip.covidapp.data.network.entity.responses.authentication.UserResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

interface BinarAuthServices {

    @POST("/api/v1/auth/register")
    suspend fun postRegisterData(@Body registerRequest: RegisterRequest): BaseAuthResponse<UserResponse, String>

    @POST("/api/v1/auth/login")
    suspend fun postLoginData(@Body loginRequest: LoginRequest): BaseAuthResponse<LoginResponse, String>

    @GET("/api/v1/auth/me")
    suspend fun getAuthData(): BaseAuthResponse<UserResponse, String>


    companion object {
        private var retrofitService: BinarAuthServices? = null
        fun getInstance(sessionPreference : SessionPreference): BinarAuthServices? {
            if (retrofitService == null) {
                val authInterceptor = Interceptor {
                    val requestBuilder = it.request().newBuilder()
                    // If token has been saved, add it to the request
                    sessionPreference.authToken?.let { token ->
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                    it.proceed(requestBuilder.build())
                }

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(authInterceptor)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL_BINAR_AUTH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                retrofitService = retrofit.create(BinarAuthServices::class.java)
            }
            return retrofitService
        }
    }
}