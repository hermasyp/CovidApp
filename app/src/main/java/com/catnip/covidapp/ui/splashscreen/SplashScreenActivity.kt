package com.catnip.covidapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.catnip.covidapp.base.GenericViewModelFactory
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.local.sharedpreference.SessionPreference
import com.catnip.covidapp.data.network.datasource.AuthDataSource
import com.catnip.covidapp.data.network.services.BinarAuthServices
import com.catnip.covidapp.databinding.ActivitySplashScreenBinding
import com.catnip.covidapp.ui.home.HomeActivity
import com.catnip.covidapp.ui.login.LoginActivity
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {
    private val TAG = SplashScreenActivity::class.java.simpleName
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var sessionPreference: SessionPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        setupViewModel()
    }

    private fun setupViewModel() {
        sessionPreference = SessionPreference(this)
        val repository = SplashScreenRepository(
            AuthDataSource(BinarAuthServices.getInstance(sessionPreference)!!)
        )
        viewModel =
            GenericViewModelFactory(SplashScreenViewModel(repository)).create(SplashScreenViewModel::class.java)
        viewModel.syncData.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "onViewCreated: loading")
                }
                is Resource.Success -> {
                    Log.d(TAG, "onViewCreated: success")
                    navigateToHome()
                }
                is Resource.Error -> {
                    Log.d(TAG, "onViewCreated: error")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    deleteSession()
                    navigateToLogin()
                }
            }
        })
        //check if there's a session exist
        if(sessionPreference.authToken != null){
            viewModel.fetchSyncData()
        }else{
            lifecycleScope.launch(Dispatchers.IO){
                delay(1000)
                lifecycleScope.launch(Dispatchers.Main){
                    navigateToLogin()
                }
            }
        }
    }

    private fun deleteSession(){
        sessionPreference.deleteSession()
    }

    private fun navigateToLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToHome(){
        val intent = Intent(this,HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()

    }
}