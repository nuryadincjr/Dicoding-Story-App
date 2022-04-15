package com.nuryadincjr.storyapp.view.register

import android.animation.AnimatorSet
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
import com.nuryadincjr.storyapp.data.remote.response.LoginResult
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.databinding.ActivityRegisterBinding
import com.nuryadincjr.storyapp.util.Constant.alphaAnim
import com.nuryadincjr.storyapp.util.Constant.transAnim
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

    private fun onResult(result: Result<PostResponse>) {
        binding.apply {
            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val registerResponse = result.data
                    val message = registerResponse.message

                    progressBar.visibility = View.GONE
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)

                    startActivity(
                        intent,
                        ActivityOptionsCompat
                            .makeSceneTransitionAnimation(this@RegisterActivity)
                            .toBundle()
                    )
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RegisterActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            val transX = transAnim(imageView, View.TRANSLATION_X)
            val transY = transAnim(imageView, View.TRANSLATION_Y)
            val launcherSet = AnimatorSet().apply {
                playTogether(transX, transY)
            }

            AnimatorSet().apply {
                playSequentially(launcherSet)
                start()
            }

            val title = alphaAnim(tvTitle)
            val name = alphaAnim(tilName)
            val email = alphaAnim(tilEmail)
            val password = alphaAnim(tilPassword)
            val register = alphaAnim(btnRegister)
            val version = alphaAnim(tvVersion)

            AnimatorSet().apply {
                playSequentially(title, name, email, password, register, version)
                start()
            }
        }
    }
}