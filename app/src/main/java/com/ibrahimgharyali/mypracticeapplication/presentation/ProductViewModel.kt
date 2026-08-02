package com.ibrahimgharyali.mypracticeapplication.presentation

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

sealed class UIState {
    data object Loading: UIState()
    data class Loaded(val products: List<Product>): UIState()
    data class Error(val message: String): UIState()
}
@HiltViewModel
class ProductViewModel @Inject constructor(private val useCase: FilteredProductuseCase): ViewModel() {

    private var _uistate : MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val uistate : StateFlow<UIState> = _uistate.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {

            useCase.loadFilteredProducts()
                .onSuccess { list -> _uistate.value = UIState.Loaded(list) }
                .onFailure { error -> _uistate.value = UIState.Error(error.message.toString()) }
        }
    }
}