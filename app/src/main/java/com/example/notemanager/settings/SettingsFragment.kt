package com.example.notemanager.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.notemanager.R

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference<ListPreference>(getString(R.string.key_pref_app_theme))?.onPreferenceChangeListener =
            this
        setupPreferenceSummery()
    }

    private fun setupPreferenceSummery() {
        findPreference<ListPreference>(getString(R.string.key_pref_app_theme))?.apply {
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                val text = it?.value
                if (text.isNullOrEmpty()) {
                    "Not Set"
                } else {
                    "App theme set to $text"
                }
            }
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        return when (preference?.key) {
            getString(R.string.key_pref_app_theme) -> {
                changeAppTheme(newValue)
                true
            }
            else -> false
        }
    }

    private fun changeAppTheme(newValue: Any?) {
        when (newValue) {
            AppTheme.DEFAULT.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.LIGHT.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppTheme.DARK.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        requireActivity().recreate()
    }
}