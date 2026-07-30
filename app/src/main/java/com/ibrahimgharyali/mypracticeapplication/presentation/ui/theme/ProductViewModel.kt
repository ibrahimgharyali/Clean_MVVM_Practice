package com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.domain.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProdState {
    data object Loading: ProdState()
    data class Loaded(val list: List<Product>): ProdState()
    data class Error(val message: String): ProdState()
}
@HiltViewModel
class ProductViewModel @Inject constructor(private val useCase: ProductUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<ProdState> = MutableStateFlow(ProdState.Loading)
    val uiState: StateFlow<ProdState> = _uiState.asStateFlow()


    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            useCase.getProductsFiltered()
                .onSuccess { prods -> _uiState.value = ProdState.Loaded(prods) }
                .onFailure { error -> _uiState.value = ProdState.Error(error.message as String) }
        }
    }
}