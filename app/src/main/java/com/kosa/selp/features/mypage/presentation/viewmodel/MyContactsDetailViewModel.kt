package com.kosa.selp.features.mypage.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.kosa.selp.features.mypage.model.ReceiverModifyRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class MyContactsDetailViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val receiverId: Long = checkNotNull(savedStateHandle["contactId"])

    private val _uiState = MutableStateFlow<MyContactsDetailUiState>(MyContactsDetailUiState.Loading)
    val uiState: StateFlow<MyContactsDetailUiState> = _uiState

    private val _event = MutableSharedFlow<MyContactsDetailEvent>()
    val event = _event.asSharedFlow()

    init {
        if (receiverId != -1L) {
            fetchContactDetail()
        } else {
            _uiState.value = MyContactsDetailUiState.Success(
                Contact(
                    id = -1,
                    nickname = "",
                    gender = "NONE",
                    relationship = "",
                    age = 0,
                    preferences = "",
                    detail = ""
                )
            )
        }
    }

    private fun fetchContactDetail() {
        viewModelScope.launch {
            _uiState.value = MyContactsDetailUiState.Loading
            try {
                val contact = myPageRepository.getMyReceiverInfoDetail(receiverId)
                _uiState.value = MyContactsDetailUiState.Success(contact)
            } catch (e: Exception) {
                _uiState.value = MyContactsDetailUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun saveContact(
        nickname: String,
        age: String,
        gender: String,
        relationship: String,
        preferences: String,
        detail: String
    ) {
        viewModelScope.launch {
            try {
                val request = ReceiverModifyRequest(
                    nickname = nickname,
                    age = age.toInt(),
                    gender = gender,
                    relationship = relationship,
                    preferences = preferences,
                    detail = detail
                )
                if (receiverId == -1L) {
                    myPageRepository.registerReceiverInfo(request)
                } else {
                    myPageRepository.modifyReceiverInfo(receiverId, request)
                }
                _event.emit(MyContactsDetailEvent.NavigateUp(true))
            } catch (e: Exception) {
                _uiState.value = MyContactsDetailUiState.Error(e.message ?: "Save failed")
            }
        }
    }

    fun deleteContact() {
        viewModelScope.launch {
            try {
                myPageRepository.deleteReceiverInfo(receiverId)
                _event.emit(MyContactsDetailEvent.NavigateUp(true))
            } catch (e: Exception) {
                _uiState.value = MyContactsDetailUiState.Error(e.message ?: "Delete failed")
            }
        }
    }
}

sealed interface MyContactsDetailEvent {
    data class NavigateUp(val needsRefresh: Boolean) : MyContactsDetailEvent
}

sealed interface MyContactsDetailUiState {
    object Loading : MyContactsDetailUiState
    data class Success(val contact: Contact) : MyContactsDetailUiState
    data class Error(val message: String) : MyContactsDetailUiState
}