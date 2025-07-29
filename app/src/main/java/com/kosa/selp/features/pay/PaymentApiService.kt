package com.kosa.selp.features.pay

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApiService {
    @POST("/payments/gift-bundles/{giftBundleId}/verify")
    suspend fun verify(
        @Path("giftBundleId") giftBundleId: Long,
        @Body
        request: PaymentVerifyRequest
    ): Response<PaymentVerifyResponse>
}