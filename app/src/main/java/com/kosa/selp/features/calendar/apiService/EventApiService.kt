package com.kosa.selp.features.calendar.apiService

import com.kosa.selp.features.calendar.dto.CalendarEvent
import com.kosa.selp.features.calendar.dto.CalendarEventModifyDto
import com.kosa.selp.features.calendar.dto.CalendarEventRegisterDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventApiService {
    @GET("events?year=2025&month=7")
    suspend fun getEvents(): List<CalendarEvent>

    @POST("events")
    suspend fun registerEvent(
        @Body
        event: CalendarEventRegisterDto
    )

    @PUT("events/{eventId}")
    suspend fun modifyEvent(
        @Path("eventId") eventId: Int,
        @Body
        event: CalendarEventModifyDto
    )

    @DELETE("events/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: Int,
    )
}