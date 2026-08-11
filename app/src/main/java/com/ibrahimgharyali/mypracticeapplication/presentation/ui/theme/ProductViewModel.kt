package com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimgharyali.mypracticeapplication.domain.FilteredProductUsecase
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UiState {
    data object Loading: UiState
    data class Loaded(val prodList: List<Product>) : UiState
    data class Error(val message: String) : UiState

}
@HiltViewModel
class ProductViewModel @Inject constructor(private val useCase: FilteredProductUsecase) : ViewModel() {

    private val _uistate : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uistate : StateFlow<UiState> = _uistate.asStateFlow()


    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.fetchFilteredData()
                .onSuccess { _uistate.value = UiState.Loaded(it)  }
                .onFailure { _uistate.value = UiState.Error(it.message?:"") }
        }
    }

    /*data class FixtureDto(
        val id: Int,
        val name: String,
        val result_info: String,
        val home_team: String,
        val away_team: String,
        val home_score: Int,
        val away_score: Int,
        val status: String,

    ) {
        fun toDomain() = Fixture(
            id = id,
            name = name,
            result =  result_info
        )
    }*/
}
