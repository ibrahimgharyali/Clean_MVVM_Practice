package com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductuseCase
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UiState {
    data object Loading: UiState
    data class Loaded(val prod: List<Product>): UiState
    data class Error(val message: String): UiState
}

@HiltViewModel
class ProductViewModel @Inject constructor(private val usecase: FilteredProductuseCase): ViewModel() {

    private val _uistate : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uistate.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            usecase.leadProducts()
                .onSuccess { pro-> _uistate.value = UiState.Loaded(pro) }
                .onFailure { error -> _uistate.value = UiState.Error(error.message?:"") }
        }
    }
}