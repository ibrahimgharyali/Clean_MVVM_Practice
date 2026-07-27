package com.ibrahimgharyali.mypracticeapplication.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {

    private val _uIState : MutableStateFlow<UIState> = MutableStateFlow(UIState.Idle())
    val uiState : StateFlow<UIState> = _uIState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbatEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()
    companion object {
        private val EMAIL_PATTERN = java.util.regex.Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        )
    }

    fun performLogin(email: String, pass: String) {
        _uIState.value = UIState.Loading
        val emailError = if(!EMAIL_PATTERN.matcher(email).matches()) "Invalid email" else null
        val passwordError = if(pass.length < 6) "Password must be at least 6 characters" else null
        if(emailError != null || passwordError != null) {
            _uIState.value = UIState.Idle(emailError, passwordError)
            return // never call API with invalid input
        }
        viewModelScope.launch {
            repository.performLogin(email, pass)
                .onSuccess { _uIState.value = UIState.Success
                    _snackbarEvent.emit("Login successful")
                }
                .onFailure { e-> _uIState.value = UIState.Error(e as? Exception ?: Exception(e)) }
        }

    }

}