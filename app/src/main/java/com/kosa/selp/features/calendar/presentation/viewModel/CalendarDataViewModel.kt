package com.kosa.selp.features.calendar.presentation.viewModel

import EventRegisterRequestDto
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.calendar.domain.usecase.GetAllEventListUseCase
import com.kosa.selp.features.calendar.domain.usecase.RegisterEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CalendarDataViewModel @Inject constructor(
    private val getAllEventListUseCase: GetAllEventListUseCase,
    private val registerEventUseCase: RegisterEventUseCase
) : ViewModel() {

    private val _eventList = MutableStateFlow<List<EventListResponseDto>>(emptyList())
    val eventList: StateFlow<List<EventListResponseDto>> = _eventList

    fun getAllEvents(year: Int, month: Int) {
        viewModelScope.launch {
            runCatching {
                getAllEventListUseCase(year, month)
            }.onSuccess { result ->
                _eventList.value = result
            }.onFailure {
                _eventList.value = emptyList()
            }
        }
    }

    fun registerEvent(request: EventRegisterRequestDto) {
        viewModelScope.launch {
            runCatching {
                registerEventUseCase(request)
            }.onSuccess {
                val now = Calendar.getInstance()
                getAllEvents(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1)
            }.onFailure {
            }
        }
    }
}
