package com.example.notemanager

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.notemanager.login.LoginActivity
import com.example.notemanager.settings.AppTheme

class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val defaultPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val themType = defaultPreference.getString(
            getString(R.string.key_pref_app_theme),
            AppTheme.DEFAULT.value
        )

        val userLoggedIn =
            defaultPreference.getBoolean(getString(R.string.key_pref_user_login), false)

        if (!userLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        setAppTheme(themType)
    }

    private fun setAppTheme(themType: String?) {
        when (themType) {
            AppTheme.DEFAULT.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.LIGHT.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DEFAULT.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}