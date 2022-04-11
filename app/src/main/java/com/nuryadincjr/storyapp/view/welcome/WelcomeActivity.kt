package com.nuryadincjr.storyapp.view.welcome

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nuryadincjr.storyapp.BuildConfig.VERSION_NAME
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.factory.UsersFactory
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.databinding.ActivityWelcomeBinding
import com.nuryadincjr.storyapp.view.login.LoginActivity
import com.nuryadincjr.storyapp.view.main.MainActivity
import com.nuryadincjr.storyapp.view.main.UsersViewModel
import com.nuryadincjr.storyapp.view.register.RegisterActivity
import okhttp3.internal.format

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    private val usersViewModel: UsersViewModel by viewModels {
        UsersFactory(UsersPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersViewModel.getUser().observe(this) {
            if (it.token.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        binding.apply {
            btnLogin.setOnClickListener(this@WelcomeActivity)
            btnRegister.setOnClickListener(this@WelcomeActivity)
        }

        setupView()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.btn_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
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
}