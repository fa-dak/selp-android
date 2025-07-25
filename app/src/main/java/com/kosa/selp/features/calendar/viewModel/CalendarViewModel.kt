package com.kosa.selp.features.calendar.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarViewModel : ViewModel() {
    private val _calendar = mutableStateOf(Calendar.getInstance())
    val calendar: State<Calendar> = _calendar

    private val _selectedDate = mutableStateOf(Date())
    val selectedDate: State<Date> = _selectedDate

    private val _isEventModalVisible = mutableStateOf(false)
    val isEventModalVisible: State<Boolean> = _isEventModalVisible

    fun goToPreviousMonth() {
        val newCalendar = _calendar.value.clone() as Calendar
        newCalendar.add(Calendar.MONTH, -1)
        _calendar.value = newCalendar
    }

    fun goToNextMonth() {
        val newCalendar = _calendar.value.clone() as Calendar
        newCalendar.add(Calendar.MONTH, 1)
        _calendar.value = newCalendar
    }

    fun onDateClick(date: Date) {

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatted = formatter.format(date)

        Log.d("CalendarViewModel", "선택한 날짜: $formatted")
        _selectedDate.value = date
        _isEventModalVisible.value = true
    }

    fun dismissModal() {
        _isEventModalVisible.value = false
    }
}