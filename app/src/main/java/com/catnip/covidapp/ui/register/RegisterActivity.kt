package com.catnip.covidapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catnip.covidapp.R
import com.catnip.covidapp.base.GenericViewModelFactory
import com.catnip.covidapp.base.Resource
import com.catnip.covidapp.data.local.sharedpreference.SessionPreference
import com.catnip.covidapp.data.network.datasource.AuthDataSource
import com.catnip.covidapp.data.network.entity.request.authentification.RegisterRequest
import com.catnip.covidapp.data.network.services.BinarAuthServices
import com.catnip.covidapp.databinding.ActivityRegisterBinding
import com.catnip.covidapp.ui.login.LoginActivity
import com.catnip.covidapp.utils.StringUtils


class RegisterActivity : AppCompatActivity() {
    private val TAG = RegisterActivity::class.java.simpleName
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setupViewModel()
        setViewClick()
    }

    private fun setupViewModel() {
        val repository = RegisterRepository(
            AuthDataSource(BinarAuthServices.getInstance(SessionPreference(this))!!)
        )
        viewModel =
            GenericViewModelFactory(RegisterViewModel(repository)).create(RegisterViewModel::class.java)
        viewModel.registerData.observe(this, {
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
                        getString(R.string.text_register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToLogin()
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
        binding.btnRegister.setOnClickListener {
            if (isFormRegisterValid()) {
                viewModel.postRegisterData(
                    RegisterRequest(
                        email = binding.etEmail.text.toString(),
                        username = binding.etUsername.text.toString(),
                        password = binding.etPassword.text.toString()
                    )
                )
            }
        }
    }
    private fun setToolbar(){
        supportActionBar?.title = getString(R.string.text_title_toolbar_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isFormRegisterValid(): Boolean {
        val email = binding.etEmail.text.toString()
        val username = binding.etUsername.text.toString()
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
        //for checking is Password empty
        if (username.isEmpty()) {
            isFormValid = false
            binding.tilUsername.isErrorEnabled = true
            binding.tilUsername.error = getString(R.string.error_username_empty)
        } else {
            binding.tilUsername.isErrorEnabled = false
        }
        return isFormValid
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun setLoadingState(isLoadingVisible: Boolean) {
        binding.pbLoading.visibility = if (isLoadingVisible) View.VISIBLE else View.GONE
    }
}