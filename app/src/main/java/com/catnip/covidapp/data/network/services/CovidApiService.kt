package com.catnip.covidapp.data.network.services

import com.catnip.covidapp.BuildConfig
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaCaseResponse
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

interface CovidApiService {

    @GET("indonesia")
    suspend fun getConfirmedCaseTotal(): List<IndonesiaCaseResponse>?

    @GET("indonesia/provinsi")
    suspend fun getConfirmedCaseByProvince(): List<IndonesiaProvinceCaseResponse>?


    companion object {
        private var retrofitService: CovidApiService? = null
        fun getInstance(): CovidApiService? {
            if (retrofitService == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL_KAWAL_CORONA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                retrofitService = retrofit.create(CovidApiService::class.java)
            }
            return retrofitService
        }
    }
}