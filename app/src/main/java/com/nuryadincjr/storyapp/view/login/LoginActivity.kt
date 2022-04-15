package com.nuryadincjr.storyapp.view.login

import android.animation.AnimatorSet
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
import com.nuryadincjr.storyapp.data.model.Users
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.remote.response.LoginResponse
import com.nuryadincjr.storyapp.data.remote.response.LoginResult
import com.nuryadincjr.storyapp.databinding.ActivityLoginBinding
import com.nuryadincjr.storyapp.util.Constant.PREF_SESSION
import com.nuryadincjr.storyapp.util.Constant.alphaAnim
import com.nuryadincjr.storyapp.util.Constant.transAnim
import com.nuryadincjr.storyapp.view.main.MainActivity
import okhttp3.internal.format

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_SESSION)

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels {
        val preference = UsersPreference.getInstance(dataStore)
        LoginFactory.getInstance(this, preference)
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

    private fun onResult(result: Result<LoginResponse>, email: String, password: String) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                val loginResponse = result.data
                val loginResult = loginResponse.loginResult as LoginResult
                val message = loginResponse.message

                loginResult.apply {
                    val users = Users(
                        userId.toString(),
                        name.toString(),
                        email, password,
                        token.toString()
                    )

                    loginViewModel.loginSession(users)
                    binding.progressBar.visibility = View.GONE
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)

                    startActivity(
                        intent,
                        ActivityOptionsCompat
                            .makeSceneTransitionAnimation(this@LoginActivity)
                            .toBundle()
                    )
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()

                    finishAffinity()
                }
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            transAnim(imageView, View.TRANSLATION_Y).start()

            val title = alphaAnim(tvTitle)
            val description = alphaAnim(tvDescription)
            val email = alphaAnim(tilEmail)
            val password = alphaAnim(tilPassword)
            val signIn = alphaAnim(btnLogin)
            val version = alphaAnim(tvVersion)

            AnimatorSet().apply {
                playSequentially(title, description, email, password, signIn, version)
                start()
            }
        }
    }
}