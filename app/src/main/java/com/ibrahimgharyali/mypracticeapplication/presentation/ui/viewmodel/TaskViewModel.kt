package com.ibrahimgharyali.mypracticeapplication.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.TaskRepository
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UIState
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
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel(){

    private val _uistate : MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState> = _uistate.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackBarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()


    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.fetchDataList()
                .onSuccess {
                    _uistate.value = UIState.Loaded(it)
                    _snackbarEvent.emit("Loaded successfully")
                }
                .onFailure { _uistate.value = UIState.Error(it as? Exception?: Exception(it)) }
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        val currentState = _uistate.value
        if (currentState is UIState.Loaded) {
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(completed = !task.completed)
                } else {
                    task
                }
            }
            _uistate.value = UIState.Loaded(updatedTasks)
        }
    }
}