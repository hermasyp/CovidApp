package com.catnip.covidapp.data.network.datasource

import com.catnip.covidapp.data.network.entity.responses.news.NewsResponse
import com.catnip.covidapp.data.network.services.NewsApiServices

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NewsDataSource(private val newsApiServices: NewsApiServices) {
    suspend fun getNews(query: String): NewsResponse = newsApiServices.getNews(query)
}