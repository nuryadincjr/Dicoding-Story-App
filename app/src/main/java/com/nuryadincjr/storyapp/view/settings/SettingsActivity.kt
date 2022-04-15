package com.nuryadincjr.storyapp.view.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.app.ActivityOptionsCompat
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.factory.SettingFactory
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.model.UsersPreference.Companion.dataStore
import com.nuryadincjr.storyapp.databinding.ActivitySettingsBinding
import com.nuryadincjr.storyapp.view.welcome.WelcomeActivity

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels {
        val preference = UsersPreference.getInstance(dataStore)
        SettingFactory.getInstance(preference)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.app_settings)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        binding.apply {
            llLanguage.setOnClickListener(this@SettingsActivity)
            switchTheme.setOnClickListener(this@SettingsActivity)
            llSignOut.setOnClickListener(this@SettingsActivity)

            settingsViewModel.getTheme().observe(this@SettingsActivity) {
                val themeMode =
                    if (it) {
                        switchTheme.isChecked = true
                        MODE_NIGHT_YES
                    } else {
                        switchTheme.isChecked = false
                        MODE_NIGHT_NO
                    }
                setDefaultNightMode(themeMode)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.switch_theme -> {
                onChangTheme()
            }
            R.id.ll_sign_out -> {
                onLogout()
            }
        }
    }

    private fun onChangTheme() {
        val isDarkMode = binding.switchTheme.isChecked
        settingsViewModel.saveTheme(isDarkMode)

        val themeMode = if (isDarkMode) MODE_NIGHT_YES else MODE_NIGHT_NO
        setDefaultNightMode(themeMode)
    }

    private fun onLogout() {
        settingsViewModel.logout()
        val intent = Intent(this, WelcomeActivity::class.java)

        startActivity(
            intent,
            ActivityOptionsCompat
                .makeSceneTransitionAnimation(this)
                .toBundle()
        )
        finishAffinity()
    }
}