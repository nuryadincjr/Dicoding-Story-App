package com.nuryadincjr.storyapp.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.model.UsersPreference.Companion.dataStore
import com.nuryadincjr.storyapp.view.main.MainActivity
import com.nuryadincjr.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    private val splashScreenViewModel: SplashScreenViewModel by viewModels {
        val preference = UsersPreference.getInstance(dataStore)
        StoriesFactory.getInstance(this, preference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setupView()
        startScreen()
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }

    private fun startScreen() {
        activityScope.launch {
            delay(3000)
            onSubscribe()
        }
    }

    private fun onSubscribe() {
        splashScreenViewModel.apply {
            getUser().observe(this@SplashScreenActivity) {
                val appCompat = if (it.token.isNotEmpty()) {
                    MainActivity::class.java
                } else {
                    WelcomeActivity::class.java
                }

                val intent = Intent(this@SplashScreenActivity, appCompat)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupView() {
        splashScreenViewModel.getTheme().observe(this@SplashScreenActivity) {
            val themeMode = if (it) MODE_NIGHT_YES else MODE_NIGHT_NO
            setDefaultNightMode(themeMode)
        }

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
    }
}