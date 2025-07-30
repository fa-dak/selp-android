//package com.kosa.selp.features.pay
//
//import android.util.Log
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import dagger.hilt.android.EntryPointAccessors
//import kotlinx.coroutines.launch
//
//@Composable
//fun PayExampleScreen() {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    var giftBundleId by remember { mutableStateOf("5") } // TODO: dev: 5 | prod: 17
//    var productName by remember { mutableStateOf("아임포트 Android SDK 결제 테스트") }
//    var amount by remember { mutableStateOf("100") }
//    var buyerName by remember { mutableStateOf("정재영") }
//    var buyerTel by remember { mutableStateOf("01012345678") }
//    var buyerEmail by remember { mutableStateOf("hanol98@naver.com") }
//    var paymentStatus by remember { mutableStateOf<String?>(null) }
//
//    fun startIamportPayment() {
//
//        val giftBundleId: Long = giftBundleId.toLongOrNull() ?: 0L
//        // 잏회용
//        val paymentManager = PaymentManager(context, coroutineScope) { statusMessage ->
//            paymentStatus = statusMessage
//        }
//
//        val appContext = context.applicationContext
//        val entryPoint = EntryPointAccessors.fromApplication(
//            appContext,
//            PaymentApiEntryPoint::class.java
//        )
//        val paymentApi = entryPoint.paymentApiService()
//
//        paymentManager.startIamportPayment(
//            giftBundleId, productName, amount, buyerName, buyerTel, buyerEmail
//        ) { impUid ->
//            paymentManager.verifyPaymentOnServer(
//                giftBundleId,
//                impUid,
//                paymentApi
//            )
//        }
//    }
//
//    fun cancelIamportPayment() {
//        val giftBundleId: Long = giftBundleId.toLongOrNull() ?: 0L
//
//        val appContext = context.applicationContext
//        val entryPoint = EntryPointAccessors.fromApplication(
//            appContext,
//            PaymentApiEntryPoint::class.java
//        )
//        val paymentApi = entryPoint.paymentApiService()
//
//        coroutineScope.launch {
//            try {
//                val response = paymentApi.cancel(giftBundleId)
//                if (response.isSuccessful) {
//                    // 취소 성공 처리
//                    Log.d("CANCEL", "결제 취소 성공")
//                } else {
//                    // 실패 응답 처리
//                    Log.w("CANCEL", "결제 취소 실패: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                Log.e("CANCEL", "결제 취소 중 오류 발생", e)
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(50.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        OutlinedTextField(
//            value = giftBundleId,
//            onValueChange = { giftBundleId = it },
//            label = { Text("상품 id") })
//        OutlinedTextField(
//            value = productName,
//            onValueChange = { productName = it },
//            label = { Text("상품명") })
//        OutlinedTextField(
//            value = amount,
//            onValueChange = { amount = it },
//            label = { Text("금액 (원)") })
//        OutlinedTextField(
//            value = buyerName,
//            onValueChange = { buyerName = it },
//            label = { Text("구매자 이름") })
//        OutlinedTextField(
//            value = buyerTel,
//            onValueChange = { buyerTel = it },
//            label = { Text("구매자 전화번호") })
//        OutlinedTextField(
//            value = buyerEmail,
//            onValueChange = { buyerEmail = it },
//            label = { Text("구매자 이메일") })
//
//        Button(onClick = { startIamportPayment() }) {
//            Text("결제하기")
//        }
//
//        Button(onClick = { cancelIamportPayment() }) {
//            Text("결제 취소하기")
//        }
//    }
//}