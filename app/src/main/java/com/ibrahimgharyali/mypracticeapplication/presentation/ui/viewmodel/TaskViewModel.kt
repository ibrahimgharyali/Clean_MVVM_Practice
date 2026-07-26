package com.ibrahimgharyali.mypracticeapplication.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.TodoRepository
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(val repository: TodoRepository): ViewModel() {

    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uistate = _uiState.asStateFlow()


    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch{
            repository.fetchTodoList()
                .onSuccess { data -> _uiState.value =  UiState.Loaded(data) }
                .onFailure { error -> _uiState.value = UiState.Error(error as? Exception ?: Exception("Unknown error")) }
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        val currentState = _uiState.value
        if (currentState is UiState.Loaded) {
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(completed = !task.completed)
                } else {
                    task
                }
            }
            _uiState.value = UiState.Loaded(updatedTasks)
        }
    }
}