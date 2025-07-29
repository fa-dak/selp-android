package com.kosa.selp.features.mypage.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.mypage.data.repository.MyPageRepository
import com.kosa.selp.features.mypage.model.Contact
import com.kosa.selp.features.mypage.model.ProductCategory
import com.kosa.selp.features.mypage.model.ReceiverModifyRequest
import com.kosa.selp.features.mypage.model.ReceiverRegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = MyContactsDetailUiState.Loading
            try {
                coroutineScope {
                    val categoriesDeferred = async { myPageRepository.getProductCategories() }
                    val contactDeferred = if (receiverId != -1L) {
                        async { myPageRepository.getMyReceiverInfoDetail(receiverId) }
                    } else {
                        null
                    }

                    val allCategories = categoriesDeferred.await()
                    Log.d("MyContactsDetailVM", "All Categories: $allCategories")

                    val contact = contactDeferred?.await() ?: Contact(
                        id = -1,
                        nickname = "",
                        gender = "NONE",
                        relationship = "",
                        age = 0,
                        preferences = emptyList(),
                        detail = ""
                    )
                    Log.d("MyContactsDetailVM", "Contact Details: $contact")

                    _uiState.value = MyContactsDetailUiState.Success(
                        contact = contact,
                        allCategories = allCategories
                    )
                }
            } catch (e: Exception) {
                Log.e("MyContactsDetailVM", "Error loading data", e)
                _uiState.value = MyContactsDetailUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onPreferenceChanged(preference: String, isSelected: Boolean) {
        val currentState = _uiState.value
        if (currentState is MyContactsDetailUiState.Success) {
            val currentPreferences = currentState.contact.preferences.toMutableList()
            if (isSelected) {
                if (!currentPreferences.contains(preference)) {
                    currentPreferences.add(preference)
                }
            } else {
                currentPreferences.remove(preference)
            }
            
            _uiState.value = currentState.copy(
                contact = currentState.contact.copy(preferences = currentPreferences)
            )
            Log.d("MyContactsDetailVM", "UPDATED Preference List: $currentPreferences")
        }
    }


    fun saveContact(
        nickname: String,
        age: String,
        gender: String,
        relationship: String,
        detail: String
    ) {
        val currentState = _uiState.value
        if (currentState !is MyContactsDetailUiState.Success) return

        viewModelScope.launch {
            try {
                val selectedPreferenceIds = currentState.allCategories
                    .filter { currentState.contact.preferences.contains(it.name) }
                    .map { it.id }

                if (receiverId == -1L) {
                    // 등록
                    val request = ReceiverRegisterRequest(
                        nickname = nickname,
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        relationship = relationship,
                        preferenceIds = selectedPreferenceIds,
                        detail = detail
                    )
                    myPageRepository.registerReceiverInfo(request)
                } else {
                    // 수정
                    val request = ReceiverModifyRequest(
                        nickname = nickname,
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        relationship = relationship,
                        preferenceIds = selectedPreferenceIds,
                        detail = detail
                    )
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
    data class Success(
        val contact: Contact,
        val allCategories: List<ProductCategory> = emptyList()
    ) : MyContactsDetailUiState
    data class Error(val message: String) : MyContactsDetailUiState
}
