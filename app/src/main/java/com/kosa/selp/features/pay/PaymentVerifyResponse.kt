package com.kosa.selp.features.pay

class PaymentVerifyResponse(
    val isValid: Boolean,
    val status: String,      // e.g., "paid"
    val amount: Double,      // 실제 결제 금액
    val merchantUid: String  // 상점 주문번호
)