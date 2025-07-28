package com.kosa.selp.features.calendar.apiService

import com.kosa.selp.features.calendar.dto.ReceiverInfo
import retrofit2.http.GET

interface ReceiverInfoApiService {
    @GET("receiver-infos")
    suspend fun getReceiverInfoList(): List<ReceiverInfo>
}
