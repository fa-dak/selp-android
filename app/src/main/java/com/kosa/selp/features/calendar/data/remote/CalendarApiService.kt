package com.kosa.selp.features.calendar.data.remote

import EventRegisterRequestDto
import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CalendarApiService {

    @GET("events")
    suspend fun getAllEventList(
        @Query("year") year: Int, @Query("month") month: Int
    ): List<EventListResponseDto>

    @POST("events")
    suspend fun registerEvent(@Body request: EventRegisterRequestDto)
}
