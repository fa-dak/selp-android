package com.kosa.selp.features.pay

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApiService {
    @POST("/payment/verify")
    suspend fun verify(
        @Body
        request: PaymentVerifyRequest
    ): Response<PaymentVerifyResponse>
}