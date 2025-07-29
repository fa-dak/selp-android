package com.kosa.selp.features.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.home.response.HomeResponseDto
import com.kosa.selp.features.home.service.HomeApiService
import com.kosa.selp.features.mypage.presentation.viewmodel.MyContactsDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: HomeApiService
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeState: StateFlow<HomeUiState> = _homeState

    init {
        viewModelScope.launch {
            try {
                val result = api.getHome()
                _homeState.value = HomeUiState.Success(result)
            } catch (e: Exception) {
                _homeState.value = HomeUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: HomeResponseDto) : HomeUiState()
    data class Error(val message: String?) : HomeUiState()
}