package com.catnip.covidapp.ui.home.covidinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.catnip.covidapp.base.GenericViewModelFactory
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.network.datasource.CovidDataSource
import com.catnip.covidapp.data.network.datasource.NewsDataSource
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaCaseResponse
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.data.network.services.CovidApiService
import com.catnip.covidapp.data.network.services.NewsApiServices
import com.catnip.covidapp.databinding.FragmentCovidInfoBinding
import com.catnip.covidapp.ui.home.HomeRepository

class CovidInfoFragment : Fragment() {

    private val TAG = CovidInfoFragment::class.java.simpleName
    private lateinit var binding: FragmentCovidInfoBinding
    private lateinit var viewModel: CovidInfoViewModel
    private lateinit var adapter: CasesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCovidInfoBinding.inflate(inflater, container, false)
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
            GenericViewModelFactory(CovidInfoViewModel(homeRepository)).create(CovidInfoViewModel::class.java)
        viewModel.homeData.observe(viewLifecycleOwner, {
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
                    it.data?.first?.let { data ->
                        bindingDataHeader(data)
                    }
                    it.data?.second?.let { data ->
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
        viewModel.fetchHomeInfo()
    }

    private fun bindingDataHeader(indonesiaCaseResponse: IndonesiaCaseResponse) {
        binding.tvTotalPositive.text = "${indonesiaCaseResponse.positif} Positive Cases"
        binding.tvTotalCured.text = "${indonesiaCaseResponse.sembuh} Recovered"
        binding.tvTotalTreated.text = "${indonesiaCaseResponse.dirawat} Treated"
        binding.tvTotalDeath.text = "${indonesiaCaseResponse.meninggal} Death"
    }

    private fun bindingDataList(data: List<IndonesiaProvinceCaseResponse>) {
        adapter.items = data
    }

    private fun setupAdapter() {
        adapter = CasesListAdapter {}
        binding.rvCovidSpread.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CovidInfoFragment.adapter
        }
    }

    private fun showContent(isContentVisible: Boolean) {
        binding.rvCovidSpread.visibility = if (isContentVisible) View.VISIBLE else View.GONE
        binding.cvHeaderCovidInfo.visibility = if (isContentVisible) View.VISIBLE else View.GONE
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
            viewModel.fetchHomeInfo()
        }
    }
}