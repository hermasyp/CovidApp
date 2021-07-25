package com.catnip.covidapp.data.network.datasource

import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaCaseResponse
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.data.network.services.CovidApiService

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class CovidDataSource(private val covidApiService: CovidApiService) {
    suspend fun getConfirmedCaseTotal(): List<IndonesiaCaseResponse>? =
        covidApiService.getConfirmedCaseTotal()

    suspend fun getConfirmedCaseByProvince(): List<IndonesiaProvinceCaseResponse>? =
        covidApiService.getConfirmedCaseByProvince()
}