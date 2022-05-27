package com.bangkit.intermediate.storyappfinal.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.intermediate.storyappfinal.R
import com.bangkit.intermediate.storyappfinal.core.util.Internet
import com.bangkit.intermediate.storyappfinal.presentation.viewmodel.UsersViewModel
import com.bangkit.intermediate.storyappfinal.core.util.Result
import com.bangkit.intermediate.storyappfinal.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    private val usersViewModel: UsersViewModel by viewModels()

    private var _authMethod: String = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUILogin()
        supportActionBar?.hide()

        binding.apply {
            btnAuth.setOnClickListener {
                if (Internet.isAvailable(this@AuthenticationActivity)) {
                    inputValidation()
                } else {
                    Toast.makeText(this@AuthenticationActivity, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
            }

            btnAcc.setOnClickListener {
                setAuthMethod()
            }
        }
    }

    private fun setUILogin() {
        binding.apply {
            resources.let {
                btnAuth.text = getString(R.string.button_login)
                btnAcc.text = getString(R.string.button_register)
                tvAcc.text = getString(R.string.ask_no_account)
                tiName.visibility = View.GONE
                etName.text = null
                etName.error = null
            }
        }
    }

    private fun setUIRegister() {
        binding.apply {
            resources.let {
                btnAuth.text = getString(R.string.button_register)
                btnAcc.text = getString(R.string.button_login)
                tvAcc.text = getString(R.string.ask_have_account)
                tiName.visibility = View.VISIBLE
            }
        }
    }

    private fun setAuthMethod() {
        when (_authMethod) {
            "Register" -> {
                setUILogin()
                _authMethod = "Login"
            }
            "Login" -> {
                setUIRegister()
                _authMethod = "Register"
            }
        }
    }

    private fun login(email: String, password: String) {
        usersViewModel.login(email, password).observe(this@AuthenticationActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.apply {
                        btnAuth.isEnabled = false
                        pbAuth.visibility = View.VISIBLE
                        btnAuth.text = ""
                    }
                    Log.d(TAG, "Loading login...")
                }
                is Result.Success -> {
                    Log.d(TAG, result.data)
                    startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                    finish()
                }
                is Result.Error -> {
                    binding.apply {
                        btnAuth.isEnabled = true
                        pbAuth.visibility = View.GONE
                        btnAuth.text = resources.getString(R.string.button_login)
                    }
                    Toast.makeText(this@AuthenticationActivity, resources.let {
                        getString(R.string.login_failed) + ".\n" +
                                getString(R.string.email) + " " +
                                getString(R.string.or) + " " +
                                getString(R.string.password) + " " +
                                getString(R.string.invalid)
                    }, Toast.LENGTH_LONG).show()
                    Log.d(TAG, result.error)
                }
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        usersViewModel.register(name, email, password).observe(this@AuthenticationActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "Loading register...")
                    binding.apply {
                        btnAuth.isEnabled = false
                        pbAuth.visibility = View.VISIBLE
                        btnAuth.text = ""
                    }
                }
                is Result.Success -> {
                    Log.d(TAG, result.data)
                    when (result.data) {
                        "User created" -> {
                            Toast.makeText(this@AuthenticationActivity, resources.getString(R.string.register_to_login), Toast.LENGTH_LONG).show()
                        }
                        "success" -> {
                            startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
                is Result.Error -> {
                    binding.apply {
                        btnAuth.isEnabled = true
                        pbAuth.visibility = View.GONE
                        btnAuth.text = resources.getString(R.string.button_register)
                    }
                    when (result.error) {
                        "HTTP 400 Bad Request" -> {
                            binding.apply {
                                etEmail.error = resources.getString(R.string.email) + " " +
                                        resources.getString(R.string.invalid) + " " +
                                        resources.getString(R.string.or) + " " +
                                        resources.getString(R.string.email_exist)
                                etEmail.requestFocus()
                            }
                        }
                        else -> {
                            Toast.makeText(this@AuthenticationActivity, resources.getString(R.string.error_occurred) + ": " + result.error, Toast.LENGTH_LONG).show()
                        }
                    }
                    Log.d(TAG, result.error)
                }
            }
        }
    }

    private fun inputValidation() {
        binding.apply {
            when (_authMethod) {
                "Login" -> {
                    when {
                        etEmail.text.isNullOrBlank() -> {
                            etEmail.error = resources.getString(R.string.required)
                            etEmail.requestFocus()
                        }
                        etPassword.text.isNullOrBlank() -> {
                            etPassword.error = resources.getString(R.string.required)
                            etPassword.requestFocus()
                        }
                        else -> {
                            if (etEmail.error.isNullOrEmpty() && etPassword.error.isNullOrEmpty()) {
                                login(etEmail.text?.trim().toString(), etPassword.text.toString())
                            }
                        }
                    }
                }
                "Register" -> {
                    when {
                        etName.text.isNullOrBlank() -> {
                            etName.error = resources.getString(R.string.required)
                            etName.requestFocus()
                        }
                        etEmail.text.isNullOrBlank() -> {
                            etEmail.error = resources.getString(R.string.required)
                            etEmail.requestFocus()
                        }
                        etPassword.text.isNullOrBlank() -> {
                            etPassword.error = resources.getString(R.string.required)
                            etPassword.requestFocus()
                        }
                        else -> {
                            if (etName.error.isNullOrEmpty() && etEmail.error.isNullOrEmpty() && etPassword.error.isNullOrEmpty()) {
                                register(etName.text.toString(), etEmail.text?.trim().toString(), etPassword.text.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AuthenticationActivity"
    }
}