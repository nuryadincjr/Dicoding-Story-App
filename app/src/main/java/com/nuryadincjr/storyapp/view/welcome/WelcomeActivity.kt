package com.nuryadincjr.storyapp.view.welcome

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nuryadincjr.storyapp.BuildConfig.VERSION_NAME
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.databinding.ActivityWelcomeBinding
import com.nuryadincjr.storyapp.util.Constant
import com.nuryadincjr.storyapp.util.Constant.alphaAnim
import com.nuryadincjr.storyapp.util.Constant.transAnim
import com.nuryadincjr.storyapp.view.login.LoginActivity
import com.nuryadincjr.storyapp.view.main.MainActivity
import com.nuryadincjr.storyapp.view.register.RegisterActivity
import okhttp3.internal.format

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constant.PREF_SESSION)

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    private val welcomeViewModel: WelcomeViewModel by viewModels {
        val preference = UsersPreference.getInstance(dataStore)
        StoriesFactory.getInstance(this, preference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        onSubscribe()
        playAnimation()
    }

    override fun onClick(p0: View?) {
        var intent: Intent? = null

        when (p0?.id) {
            R.id.btn_login -> {
                intent = Intent(this, LoginActivity::class.java)
            }
            R.id.btn_register -> {
                intent = Intent(this, RegisterActivity::class.java)
            }
        }

        if (intent != null) {
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")

        welcomeViewModel.getTheme().observe(this@WelcomeActivity) {
            val themeMode = if (it) MODE_NIGHT_YES else MODE_NIGHT_NO
            setDefaultNightMode(themeMode)
        }

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
            btnLogin.setOnClickListener(this@WelcomeActivity)
            btnRegister.setOnClickListener(this@WelcomeActivity)
        }
    }

    private fun playAnimation() {
        binding.apply {
            transAnim(imageView, View.TRANSLATION_X).start()

            val title = alphaAnim(tvTitle)
            val description = alphaAnim(tvDescription)
            val signIn = alphaAnim(btnLogin)
            val register = alphaAnim(btnRegister)
            val version = alphaAnim(tvVersion)

            val buttonSet = AnimatorSet().apply {
                playTogether(signIn, register)
            }

            AnimatorSet().apply {
                playSequentially(title, description, buttonSet, version)
                start()
            }
        }
    }

    private fun onSubscribe() {
        welcomeViewModel.apply {
            getUser().observe(this@WelcomeActivity) {
                if (it.token.isNotEmpty()) {
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}