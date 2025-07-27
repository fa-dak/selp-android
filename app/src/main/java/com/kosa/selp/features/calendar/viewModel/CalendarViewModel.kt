package com.kosa.selp.features.calendar.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosa.selp.BuildConfig
import com.kosa.selp.features.calendar.apiService.EventApiService
import com.kosa.selp.features.calendar.dto.CalendarEvent
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarViewModel : ViewModel() {
    // 현재 달력을 저장하는 상태 변수
    private val _calendar = mutableStateOf(Calendar.getInstance())
    val calendar: State<Calendar> = _calendar

    // 선택된 날짜를 저장하는 상태 변수
    private val _selectedDate = mutableStateOf(Date())
    val selectedDate: State<Date> = _selectedDate

    // 이벤트 모달의 표시 여부를 저장하는 상태 변수
    private val _isEventModalVisible = mutableStateOf(false)
    val isEventModalVisible: State<Boolean> = _isEventModalVisible

    // 하루짜리 이벤트 리스트 (예시)
    private val _events = mutableStateOf<List<CalendarEvent>>(emptyList())
    val events: State<List<CalendarEvent>> = _events

    // 선택된 날짜의 이벤트만 반환
    val selectedDayEvents: List<CalendarEvent>
        get() = _events.value.filter { event ->
            val cal1 = Calendar.getInstance().apply { time = event.eventDate }
            val cal2 = Calendar.getInstance().apply { time = _selectedDate.value }
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
        }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_BASE_URL) // 실제 API base url로 변경
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(EventApiService::class.java)

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val apiEvents = api.getEvents()
                Log.d("CalendarViewModel", "잘 가져옴: $apiEvents")
                if (apiEvents.isNotEmpty()) {
                    _events.value = apiEvents
                }
            } catch (e: Exception) {
                // 실패 시 기존 더미 데이터 유지
                Log.d("CalendarViewModel", "실패: $e")
                _events.value = listOf(
                    CalendarEvent(
                        eventId = "1",
                        eventName = "가족 여행",
                        eventType = "여행",
                        receiverNickname = "세영이",
                        notificationDaysBefore = 1,
                        eventDate = SimpleDateFormat("yyyy-MM-dd").parse("2025-07-01")!!
                    ),
                    CalendarEvent(
                        eventId = "2",
                        eventName = "쇼핑",
                        eventType = "기념일",
                        receiverNickname = "엄마아빠",
                        notificationDaysBefore = 3,
                        eventDate = SimpleDateFormat("yyyy-MM-dd").parse("2025-07-10")!!
                    )
                )
            }
        }
    }

    // 이전 달로 이동하는 함수
    fun goToPreviousMonth() {
        val newCalendar = _calendar.value.clone() as Calendar
        newCalendar.add(Calendar.MONTH, -1)
        _calendar.value = newCalendar
    }

    // 다음 달로 이동하는 함수
    fun goToNextMonth() {
        val newCalendar = _calendar.value.clone() as Calendar
        newCalendar.add(Calendar.MONTH, 1)
        _calendar.value = newCalendar
    }

    fun onDateClick(date: Date) {
        val sameDay = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date) ==
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(_selectedDate.value)

        if (sameDay) {
            _isEventModalVisible.value = false
        } else {
            _selectedDate.value = date
            _isEventModalVisible.value = true
        }
    }

    // 모달을 닫는 함수
    fun dismissModal() {
        _isEventModalVisible.value = false
    }


}