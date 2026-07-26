package com.ibrahimgharyali.mypracticeapplication.presentation.ui

import com.ibrahimgharyali.mypracticeapplication.domain.Tasks

sealed class UIState {
    object Loading: UIState()
    data class Loaded(val tasks: List<Tasks>): UIState()
    data class Error(val e: Exception): UIState()
}