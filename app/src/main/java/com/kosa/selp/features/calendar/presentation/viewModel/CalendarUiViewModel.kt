package com.kosa.selp.features.calendar.presentation.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarUiViewModel @Inject constructor() : ViewModel() {

    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    private val _currentMonth = MutableStateFlow(Calendar.getInstance())
    val currentMonth: StateFlow<Calendar> = _currentMonth

    fun moveToPreviousMonth() {
        val newCalendar = (_currentMonth.value.clone() as Calendar).apply {
            add(Calendar.MONTH, -1)
        }
        _currentMonth.value = newCalendar
    }

    fun moveToNextMonth() {
        val newCalendar = (_currentMonth.value.clone() as Calendar).apply {
            add(Calendar.MONTH, 1)
        }
        _currentMonth.value = newCalendar
    }

    fun setCurrentMonth(calendar: Calendar) {
        _currentMonth.value = calendar.clone() as Calendar
    }
}
