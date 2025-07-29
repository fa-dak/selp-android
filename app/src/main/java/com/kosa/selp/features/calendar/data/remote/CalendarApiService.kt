package com.kosa.selp.features.calendar.data.remote

import com.kosa.selp.features.calendar.data.response.EventListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApiService {

    @GET("events")
    suspend fun getAllEventList(
        @Query("year") year: Int, @Query("month") month: Int
    ): List<EventListResponseDto>
}
