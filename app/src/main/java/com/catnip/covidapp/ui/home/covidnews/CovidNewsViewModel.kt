package com.catnip.covidapp.ui.home.covidnews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaCaseResponse
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.data.network.entity.responses.news.NewsResponse
import com.catnip.covidapp.ui.home.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class CovidNewsViewModel(private val repository: HomeRepository) : ViewModel() {
    val newsData =
        MutableLiveData<Resource<NewsResponse>>()

    fun fetchNews(query : String) {
        newsData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getNews(query)
                viewModelScope.launch(Dispatchers.Main) {
                    newsData.value = Resource.Success(response)
                }
            }catch (e : Exception){
                viewModelScope.launch(Dispatchers.Main) {
                    newsData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }
}