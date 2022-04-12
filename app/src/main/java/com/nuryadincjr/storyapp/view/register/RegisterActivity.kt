package com.nuryadincjr.storyapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator.*
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
import com.nuryadincjr.storyapp.BuildConfig.VERSION_NAME
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.factory.RegisterFactory
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.databinding.ActivityRegisterBinding
import com.nuryadincjr.storyapp.view.login.LoginActivity
import okhttp3.internal.format

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_register) onRegister()
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
            btnRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    private fun onRegister() {
        binding.apply {
            val name = tietName.text.toString()
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()
            val isEmailError = tietEmail.error.isNullOrEmpty()
            val isPasswordError = tietPassword.error.isNullOrEmpty()

            if (isEmailError && isPasswordError) {
                when {
                    name.isEmpty() -> {
                        tilName.error = getString(R.string.error_name_empty)
                    }
                    email.isEmpty() -> {
                        tilEmail.error = getString(R.string.error_email_empty)
                    }
                    password.isEmpty() -> {
                        tilPassword.error = getString(R.string.error_password_empty)
                    }
                    else -> {
                        progressBar.visibility = View.VISIBLE
                        registerViewModel.apply {
                            onRegister(name, email, password).observe(this@RegisterActivity) {
                                onResult(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onResult(response: Result<PostResponse>) {
        binding.apply {
            when (response) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)

                    startActivity(
                        intent,
                        ActivityOptionsCompat
                            .makeSceneTransitionAnimation(this@RegisterActivity)
                            .toBundle()
                    )
                    finish()
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RegisterActivity, response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            val transX = ofFloat(imageView, View.TRANSLATION_X, 30f, -30f).apply {
                duration = 6000
                repeatCount = INFINITE
                repeatMode = REVERSE
            }

            val transY = ofFloat(imageView, View.TRANSLATION_Y, 30f, -30f).apply {
                duration = 6000
                repeatCount = INFINITE
                repeatMode = REVERSE
            }

            val launcherSet = AnimatorSet().apply {
                playTogether(transX, transY)
            }

            AnimatorSet().apply {
                playSequentially(launcherSet)
                start()
            }

            val title = ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
            val name = ofFloat(tilName, View.ALPHA, 1f).setDuration(500)
            val email = ofFloat(tilEmail, View.ALPHA, 1f).setDuration(500)
            val password = ofFloat(tilPassword, View.ALPHA, 1f).setDuration(500)
            val register = ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500)
            val version = ofFloat(tvVersion, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(title, name, email, password, register, version)
                start()
            }
        }
    }
}