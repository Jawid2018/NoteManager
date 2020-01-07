package com.example.notemanager.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notemanager.utils.Event

class LoginViewModel : ViewModel() {

    private var _onLoginButtonClick = MutableLiveData<Event<Unit>>()
    val onLoginButtonClick: LiveData<Event<Unit>>
        get() = _onLoginButtonClick

    fun onLoginButtonClick() {
        _onLoginButtonClick.value = Event(Unit)
    }
}