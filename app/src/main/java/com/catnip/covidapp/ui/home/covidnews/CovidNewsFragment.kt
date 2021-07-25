package com.catnip.covidapp.ui.home.covidnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.catnip.covidapp.R
import com.catnip.covidapp.base.GenericViewModelFactory
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.datasource.CovidDataSource
import com.catnip.covidapp.data.network.datasource.NewsDataSource
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.data.network.entity.responses.news.News
import com.catnip.covidapp.data.network.services.CovidApiService
import com.catnip.covidapp.data.network.services.NewsApiServices
import com.catnip.covidapp.databinding.FragmentCovidInfoBinding
import com.catnip.covidapp.databinding.FragmentCovidNewsBinding
import com.catnip.covidapp.ui.home.HomeRepository
import com.catnip.covidapp.ui.home.covidinfo.CasesListAdapter
import com.catnip.covidapp.ui.home.covidinfo.CovidInfoFragment
import com.catnip.covidapp.ui.home.covidinfo.CovidInfoViewModel
import com.catnip.covidapp.utils.Constants
import com.catnip.covidapp.utils.IntentUtils
import java.net.URLEncoder

class CovidNewsFragment : Fragment() {
    private val TAG = CovidNewsFragment::class.java.simpleName
    private lateinit var binding: FragmentCovidNewsBinding
    private lateinit var viewModel: CovidNewsViewModel
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCovidNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        setupSwipeRefresh()
    }

    private fun setupViewModel() {
        val homeRepository = HomeRepository(
            CovidDataSource(CovidApiService.getInstance()!!),
            NewsDataSource(NewsApiServices.getInstance()!!)
        )
        viewModel =
            GenericViewModelFactory(CovidNewsViewModel(homeRepository)).create(CovidNewsViewModel::class.java)
        viewModel.newsData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "onViewCreated: loading")
                    showLoading(true)
                    showContent(false)
                    showErrors(null,false)
                }
                is Resource.Success -> {
                    Log.d(TAG, "onViewCreated: success")
                    showLoading(false)
                    showContent(true)
                    showErrors(null,false)

                    it.data?.response?.results?.let { data ->
                        bindingDataList(data)
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, "onViewCreated: error")
                    showLoading(false)
                    showContent(false)
                    showErrors(it.message,true)
                }
            }
        })
        viewModel.fetchNews(URLEncoder.encode(Constants.NEWS_QUERY,"UTF-8"))
    }


    private fun bindingDataList(data: List<News>) {
        adapter.items = data
    }

    private fun setupAdapter() {
        adapter = NewsListAdapter {
            IntentUtils.openToBrowser(context,it.webUrl.orEmpty())
        }
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CovidNewsFragment.adapter
        }
    }

    private fun showContent(isContentVisible: Boolean) {
        binding.rvNews.visibility = if (isContentVisible) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrors(msg: String?,isErrorVisible : Boolean){
        binding.tvErrorMessage.visibility =  if (isErrorVisible) View.VISIBLE else View.GONE
        binding.tvErrorMessage.text = msg
    }
    private fun setupSwipeRefresh(){
        binding.srlContent.setOnRefreshListener {
            binding.srlContent.isRefreshing = false
            viewModel.fetchNews(URLEncoder.encode(Constants.NEWS_QUERY,"UTF-8"))
        }
    }

}