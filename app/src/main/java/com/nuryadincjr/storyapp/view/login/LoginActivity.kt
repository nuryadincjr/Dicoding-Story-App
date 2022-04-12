package com.nuryadincjr.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator.*
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
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nuryadincjr.storyapp.BuildConfig.VERSION_NAME
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

        setupView()
        playAnimation()
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

        binding.apply {
            tvVersion.text = format(getString(R.string.version, VERSION_NAME))
            btnLogin.setOnClickListener(this@LoginActivity)
        }
    }

    private fun onLogin() {
        binding.apply {
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()
            val isEmailError = tietEmail.error.isNullOrEmpty()
            val isPasswordError = tietPassword.error.isNullOrEmpty()

            if (isEmailError && isPasswordError) {
                when {
                    email.isEmpty() -> {
                        tilEmail.error = getString(R.string.error_email_empty)
                    }
                    password.isEmpty() -> {
                        tilPassword.error = getString(R.string.error_password_empty)
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
                val intent = Intent(this, MainActivity::class.java)

                startActivity(
                    intent,
                    ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this)
                        .toBundle()
                )
                finishAffinity()
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            ofFloat(imageView, View.TRANSLATION_Y, 30f, -30f).apply {
                duration = 6000
                repeatCount = INFINITE
                repeatMode = REVERSE
            }.start()

            val title = ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
            val description = ofFloat(tvDescription, View.ALPHA, 1f).setDuration(500)
            val email = ofFloat(tilEmail, View.ALPHA, 1f).setDuration(500)
            val password = ofFloat(tilPassword, View.ALPHA, 1f).setDuration(500)
            val signIn = ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)
            val version = ofFloat(tvVersion, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(title, description, email, password, signIn, version)
                start()
            }
        }
    }
}