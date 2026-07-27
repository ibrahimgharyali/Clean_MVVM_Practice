package com.ibrahimgharyali.mypracticeapplication.presentation.ui

sealed class UIState {
    data class Idle(val emailError: String? = null, val passwordError: String? = null): UIState()
    object Loading: UIState()
    object Success: UIState()
    data class Error(val e: Exception): UIState()
}