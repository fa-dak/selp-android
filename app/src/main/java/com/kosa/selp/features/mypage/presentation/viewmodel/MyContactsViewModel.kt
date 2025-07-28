package com.kosa.selp.features.mypage.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContactsViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyContactsUiState>(MyContactsUiState.Loading)
    val uiState: StateFlow<MyContactsUiState> = _uiState

    init {
        fetchMyContacts()
    }

    private fun fetchMyContacts() {
        viewModelScope.launch {
            _uiState.value = MyContactsUiState.Loading
            try {
                val contacts = myPageRepository.getMyReceiverInfoList()
                _uiState.value = MyContactsUiState.Success(contacts)
            } catch (e: Exception) {
                _uiState.value = MyContactsUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed interface MyContactsUiState {
    object Loading : MyContactsUiState
    data class Success(val contacts: List<Contact>) : MyContactsUiState
    data class Error(val message: String) : MyContactsUiState
}
