package com.example.notemanager.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.notemanager.R
import com.example.notemanager.home.HomeActivity
import com.example.notemanager.utils.EventObserver
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        btn_login.setOnClickListener {
            viewModel.onLoginButtonClick()
        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.onLoginButtonClick.observe(this, EventObserver {
            navigateToHomeActivity()
        })
    }

    private fun navigateToHomeActivity() {
        PreferenceManager.getDefaultSharedPreferences(this).edit {
            putBoolean(getString(R.string.key_pref_user_login), true)
        }

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
