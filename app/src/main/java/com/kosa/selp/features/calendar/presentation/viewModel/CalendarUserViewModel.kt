package com.kosa.selp.features.calendar.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.shared.data.manager.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarUserViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _memberId = MutableStateFlow<Long?>(null)
    val memberId: StateFlow<Long?> = _memberId

    init {
        viewModelScope.launch {
            sessionManager.currentUser.collect { user ->
                _memberId.value = user?.userId
            }
        }
    }
}
