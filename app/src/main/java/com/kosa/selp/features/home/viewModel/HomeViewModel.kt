package com.kosa.selp.features.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.home.response.HomeResponseDto
import com.kosa.selp.features.home.service.HomeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: HomeApiService
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeResponseDto?>(null)
    val homeState: StateFlow<HomeResponseDto?> = _homeState

    init {
        viewModelScope.launch {
            try {
                _homeState.value = api.getHome()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "불러오기 실패", e)
            }
        }
    }
}