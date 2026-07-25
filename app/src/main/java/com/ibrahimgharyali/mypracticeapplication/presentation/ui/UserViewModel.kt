package com.ibrahimgharyali.mypracticeapplication.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.model.User
import com.ibrahimgharyali.mypracticeapplication.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel(){
    private val _uistate: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading) // initial value
    val uiState : StateFlow<UiState> = _uistate.asStateFlow()

    init {
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            _uistate.value = UiState.Loading
            repository.getUsers()
                .onSuccess{ users -> _uistate.value = UiState.Loaded(users) }
                .onFailure { e -> _uistate.value = UiState.Error(e as? Exception ?: Exception(e)) }
        }
    }
}