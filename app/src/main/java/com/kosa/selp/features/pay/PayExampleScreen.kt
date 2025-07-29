package com.kosa.selp.features.pay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kosa.selp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PayExampleScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var productName by remember { mutableStateOf("아임포트 Android SDK 결제 테스트") }
    var amount by remember { mutableStateOf("100") }
    var buyerName by remember { mutableStateOf("홍길동") }
    var buyerTel by remember { mutableStateOf("01012345678") }
    var buyerEmail by remember { mutableStateOf("hanol98@naver.com") }
    var paymentStatus by remember { mutableStateOf<String?>(null) }

    fun startIamportPayment() {
        // 잏회용
        val paymentManager = PaymentManager(context, coroutineScope) { statusMessage ->
            paymentStatus = statusMessage
        }

        paymentManager.startIamportPayment(
            productName, amount, buyerName, buyerTel, buyerEmail
        ) { impUid ->

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BACKEND_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val paymentApi = retrofit.create(PaymentApiService::class.java)
            paymentManager.verifyPaymentOnServer(impUid, paymentApi)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("상품명") })
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("금액 (원)") })
        OutlinedTextField(
            value = buyerName,
            onValueChange = { buyerName = it },
            label = { Text("구매자 이름") })
        OutlinedTextField(
            value = buyerTel,
            onValueChange = { buyerTel = it },
            label = { Text("구매자 전화번호") })
        OutlinedTextField(
            value = buyerEmail,
            onValueChange = { buyerEmail = it },
            label = { Text("구매자 이메일") })

        Button(onClick = { startIamportPayment() }) {
            Text("결제하기")
        }
    }
}