package com.ibrahimgharyali.mypracticeapplication.presentation.ui

import com.ibrahimgharyali.mypracticeapplication.domain.Tasks

sealed class UiState {
    object Loading: UiState()
    data class Loaded(val tasks: List<Tasks>): UiState()
    data class Error(val e: Exception): UiState()
}