package com.ibrahimgharyali.mypracticeapplication.presentation.ui

import com.ibrahimgharyali.mypracticeapplication.domain.model.User

sealed class UiState {
    data object Loading: UiState()
    data class Loaded(val users: List<User>, val isRefreshing: Boolean): UiState()
    data class Error(val e: Exception): UiState()
}