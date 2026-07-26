package com.ibrahimgharyali.mypracticeapplication.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.repository.UserRepository
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
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel(){
    private val _uistate: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading) // initial value
    val uiState : StateFlow<UiState> = _uistate.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackBarEvent : SharedFlow<String> = _snackbarEvent.asSharedFlow()

    init {
        loadData()
    }
    fun loadData(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if(isRefreshing) {
                val currentState = _uistate.value
                if(currentState is UiState.Loaded) _uistate.value  = currentState.copy(isRefreshing = true)

            } else {
                _uistate.value = UiState.Loading
            }
            repository.getUsers()
                .onSuccess{ users -> _uistate.value = UiState.Loaded(users, isRefreshing = false) } // toggle isRefreshing
                .onFailure { e ->
                    if(isRefreshing) {
                        _snackbarEvent.emit(e.message?:"Error Refreshing user list")
                        val currentState = _uistate.value
                        if(currentState is UiState.Loaded) _uistate.value = currentState.copy(isRefreshing = false)
                    } else {
                        _uistate.value = UiState.Error(e as? Exception ?: Exception(e))
                    }
                }
        }
    }
}