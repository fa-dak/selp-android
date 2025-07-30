package com.kosa.selp.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.home.response.HomeResponseDto
import com.kosa.selp.features.home.service.HomeApiService
import com.kosa.selp.features.notification.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: HomeApiService,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeState: StateFlow<HomeUiState> = _homeState

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount

    init {
        viewModelScope.launch {
            loadHome()
            loadUnreadCount()
        }
    }

    private fun loadHome() {
        viewModelScope.launch {
            try {
                val result = api.getHome()
                _homeState.value = HomeUiState.Success(result)
            } catch (e: Exception) {
                _homeState.value = HomeUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun loadUnreadCount() {
        viewModelScope.launch {
            runCatching {
                notificationRepository.getUnreadCount()
            }.onSuccess {
                _unreadCount.value = it
            }.onFailure {
                _unreadCount.value = 0
            }
        }
    }

}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: HomeResponseDto) : HomeUiState()
    data class Error(val message: String?) : HomeUiState()
}