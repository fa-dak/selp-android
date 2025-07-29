package com.kosa.selp.features.pay

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PaymentApiEntryPoint {
    fun paymentApiService(): PaymentApiService
}