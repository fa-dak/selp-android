package com.kosa.selp.features.calendar.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import com.kosa.selp.features.calendar.domain.usecase.GetAllEventListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarDataViewModel @Inject constructor(
    private val getAllEventListUseCase: GetAllEventListUseCase
) : ViewModel() {

    private val _eventList = MutableStateFlow<List<EventListResponseDto>>(emptyList())
    val eventList: StateFlow<List<EventListResponseDto>> = _eventList

    fun getAllEvents(year: Int, month: Int) {
        viewModelScope.launch {
            runCatching {
                getAllEventListUseCase(year, month)
            }.onSuccess { result ->
                _eventList.value = result
                Log.i("AAACACSACASDSASA", "${result}")
            }.onFailure {
                Log.i("CalendarDataViewModel", "getAllEvents: $it")
                _eventList.value = emptyList()
            }
        }
    }
}
