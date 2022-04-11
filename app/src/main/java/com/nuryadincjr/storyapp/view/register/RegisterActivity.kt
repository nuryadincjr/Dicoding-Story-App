package com.nuryadincjr.storyapp.view.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        binding.btnRegister.setOnClickListener(this)

        setupView()
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

        binding.tvVersion.text = format(getString(R.string.version, VERSION_NAME))
    }

    private fun onRegister() {
        binding.apply {
            val name = tietName.text.toString()
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()

            when {
                name.isEmpty() -> {
                    tilName.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    tilEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    tilPassword.error = "Masukkan password"
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

    private fun onResult(response: Result<PostResponse>) {
        binding.apply {
            when (response) {
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar.visibility = View.GONE
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RegisterActivity, response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}