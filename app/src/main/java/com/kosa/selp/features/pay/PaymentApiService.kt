package com.kosa.selp.features.pay

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApiService {
    @POST("/payments/products/{productId}")
    suspend fun verify(
        @Path("productId") productId: Long,
        @Body
        request: PaymentVerifyRequest
    ): Response<PaymentVerifyResponse>
}