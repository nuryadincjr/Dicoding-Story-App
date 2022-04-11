package com.nuryadincjr.storyapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nuryadincjr.storyapp.BuildConfig
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.factory.LoginFactory
import com.nuryadincjr.storyapp.data.factory.UsersFactory
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.remote.response.LoginResult
import com.nuryadincjr.storyapp.databinding.ActivityLoginBinding
import com.nuryadincjr.storyapp.view.main.MainActivity
import com.nuryadincjr.storyapp.view.main.UsersViewModel
import okhttp3.internal.format

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        LoginFactory.getInstance(this)
    }

    private val usersViewModel: UsersViewModel by viewModels {
        UsersFactory(UsersPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)

        setupView()
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_login) onLogin()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        binding.tvVersion.text =
            format(getString(R.string.version, BuildConfig.VERSION_NAME))
    }

    private fun onLogin() {
        binding.apply {
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()

            when {
                email.isEmpty() -> {
                    tilEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    tilPassword.error = "Masukkan password"
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    loginViewModel.apply {
                        onLogin(email, password).observe(this@LoginActivity) {
                            onResult(it, email, password)
                        }
                    }
                }
            }
        }
    }

    private fun onResult(response: Result<LoginResult>, email: String, password: String) {
        when (response) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                response.data.apply {
                    val users =
                        Users(userId.toString(), name.toString(), email, password, token.toString())
                    usersViewModel.login(users)
                }

                binding.progressBar.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}