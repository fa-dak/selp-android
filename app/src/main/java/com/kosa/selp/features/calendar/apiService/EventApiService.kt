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
import retrofit2.http.Query

interface EventApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<CalendarEvent>

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