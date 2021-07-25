package com.catnip.covidapp.ui.home

import com.catnip.covidapp.data.network.datasource.CovidDataSource
import com.catnip.covidapp.data.network.datasource.NewsDataSource
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaCaseResponse
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.data.network.entity.responses.news.NewsResponse

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class HomeRepository(
    private val covidDataSource: CovidDataSource,
    private val newsDataSource: NewsDataSource
) {
    suspend fun getHomeData(): Pair<IndonesiaCaseResponse?, List<IndonesiaProvinceCaseResponse>?> {
        return Pair(
            covidDataSource.getConfirmedCaseTotal()?.get(0),
            covidDataSource.getConfirmedCaseByProvince()
        )
    }

    suspend fun getNews(query: String): NewsResponse {
        return newsDataSource.getNews(query)
    }

}