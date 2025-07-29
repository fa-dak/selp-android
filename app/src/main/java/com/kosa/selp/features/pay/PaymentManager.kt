package com.kosa.selp.features.pay

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.iamport.sdk.data.sdk.IamPortRequest
import com.iamport.sdk.data.sdk.PG
import com.iamport.sdk.data.sdk.PayMethod
import com.iamport.sdk.domain.core.Iamport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PaymentManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val updateStatus: (String) -> Unit = {}
) {

    fun startIamportPayment(
        productName: String,
        amount: String,
        buyerName: String,
        buyerTel: String,
        buyerEmail: String,
        onVerify: (String) -> Unit
    ) {
        Iamport.payment(
            userCode = "imp70135330",
            iamPortRequest = IamPortRequest(
                pg = PG.html5_inicis.name,
                pay_method = PayMethod.card.name,
                name = productName,
                merchant_uid = "pay_${System.currentTimeMillis()}",
                amount = (amount.toDoubleOrNull() ?: 0.0).toString(),
                buyer_name = buyerName,
                buyer_tel = buyerTel,
                buyer_email = buyerEmail,
                app_scheme = null
            ),
            approveCallback = null,
            paymentResultCallback = { result ->
                Log.d("PAYMENT_CALLBACK", "결제 결과: $result")

                if (result != null && result.imp_success == true && result.imp_uid != null) {
                    val impUid: String = result.imp_uid!!
                    Log.d("PAYMENT_CALLBACK", "성공: imp_uid=$impUid")
                    onVerify(impUid)
                } else {
                    Toast.makeText(context, "결제 실패: ${result?.error_msg}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }

    fun verifyPaymentOnServer(impUid: String, paymentApiService: PaymentApiService) {
        coroutineScope.launch {
            updateStatus("결제 검증 중...")
            val response = paymentApiService.verify(PaymentVerifyRequest(impUid))

            if (response.isSuccessful && response.body()?.isValid == true) {
                updateStatus("결제 완료!")
                Toast.makeText(context, "결제 완료!", Toast.LENGTH_SHORT).show()
            } else {
                updateStatus("결제 검증 실패")
                Toast.makeText(context, "결제 검증 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}