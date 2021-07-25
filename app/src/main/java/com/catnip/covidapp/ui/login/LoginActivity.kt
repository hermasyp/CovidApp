package com.catnip.covidapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catnip.covidapp.R
import com.catnip.covidapp.base.GenericViewModelFactory
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.local.sharedpreference.SessionPreference
import com.catnip.covidapp.data.network.datasource.AuthDataSource
import com.catnip.covidapp.data.network.entity.request.authentification.LoginRequest
import com.catnip.covidapp.data.network.services.BinarAuthServices
import com.catnip.covidapp.databinding.ActivityLoginBinding
import com.catnip.covidapp.ui.home.HomeActivity
import com.catnip.covidapp.ui.register.RegisterActivity
import com.catnip.covidapp.utils.StringUtils

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.text_title_toolbar_login)
        setContentView(binding.root)
        setupViewModel()
        setViewClick()
    }

    private fun setupViewModel() {
        val repository = LoginRepository(
            AuthDataSource(BinarAuthServices.getInstance(SessionPreference(this))!!)
        )
        viewModel =
            GenericViewModelFactory(LoginViewModel(repository)).create(LoginViewModel::class.java)
        viewModel.loginData.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "onViewCreated: loading")
                    setLoadingState(true)
                }
                is Resource.Success -> {
                    Log.d(TAG, "onViewCreated: success")
                    setLoadingState(false)
                    Toast.makeText(
                        this,
                        getString(R.string.text_login_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    it.data?.let { data ->
                        SessionPreference(this).authToken = data.token
                    }
                    navigateToHome()
                }
                is Resource.Error -> {
                    setLoadingState(false)
                    Log.d(TAG, "onViewCreated: error")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun setViewClick() {
        binding.btnLogin.setOnClickListener {
            if (isFormLoginValid()) {
                viewModel.postLoginData(
                    LoginRequest(
                        email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString()
                    )
                )
            }
        }
        binding.btnNavigateRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun isFormLoginValid(): Boolean {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        var isFormValid = true

        //for checking is email empty
        when {
            email.isEmpty() -> {
                isFormValid = false
                binding.tilEmail.isErrorEnabled = true
                binding.tilEmail.error = getString(R.string.error_email_empty)
            }
            StringUtils.isValidEmail(email).not() -> {
                isFormValid = false
                binding.tilEmail.isErrorEnabled = true
                binding.tilEmail.error = getString(R.string.error_email_invalid)
            }
            else -> {
                binding.tilEmail.isErrorEnabled = false
            }
        }
        //for checking is Password empty
        if (password.isEmpty()) {
            isFormValid = false
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = getString(R.string.error_password_empty)
        } else {
            binding.tilPassword.isErrorEnabled = false
        }
        return isFormValid
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun setLoadingState(isLoadingVisible: Boolean) {
        binding.pbLoading.visibility = if (isLoadingVisible) View.VISIBLE else View.GONE
    }
}