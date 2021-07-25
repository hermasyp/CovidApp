package com.catnip.covidapp.ui.home.covidinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaCaseResponse
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.ui.home.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class CovidInfoViewModel(private val repository: HomeRepository) : ViewModel() {
    val homeData =
        MutableLiveData<Resource<Pair<IndonesiaCaseResponse?, List<IndonesiaProvinceCaseResponse>?>>>()

    fun fetchHomeInfo() {
        homeData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getHomeData()
                viewModelScope.launch(Dispatchers.Main) {
                    homeData.value = Resource.Success(response)
                }
            }catch (e : Exception){
                viewModelScope.launch(Dispatchers.Main) {
                    homeData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }
}