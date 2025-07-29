package com.kosa.selp.features.home.service

import com.kosa.selp.features.home.response.HomeResponseDto
import retrofit2.http.GET

interface HomeApiService {
    @GET("/api/home/main")
    suspend fun getHome(): HomeResponseDto
}