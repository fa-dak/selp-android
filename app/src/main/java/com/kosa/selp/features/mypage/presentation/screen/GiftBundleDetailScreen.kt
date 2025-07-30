package com.kosa.selp.features.mypage.presentation.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kosa.selp.features.mypage.model.GiftBundleResponse
import com.kosa.selp.features.mypage.presentation.viewmodel.MyPageViewModel
import com.kosa.selp.features.pay.CustomResultDialog
import com.kosa.selp.features.pay.PayStatus
import com.kosa.selp.features.pay.PaymentApiEntryPoint
import com.kosa.selp.features.pay.PaymentManager
import com.kosa.selp.shared.theme.AppColor
import com.kosa.selp.shared.theme.Primary
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBundleDetailScreen(
    bundleId: Long,
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val giftBundleDetail by viewModel.giftBundleDetail.collectAsStateWithLifecycle()

    val giftBundleId: Long = giftBundleDetail?.giftBundleId ?: 0L
    val productName: String = "선물 꾸러미_${giftBundleId}번"
    val amount: Long = giftBundleDetail?.products?.sumOf { it.price } ?: 0
    val buyerName: String = giftBundleDetail?.receiverNickname ?: "알 수 없음"
    val buyerTel: String = "01050926683"
    val buyerEmail: String = "selp@gmail.com"
    val currentPayStatus: PayStatus = giftBundleDetail?.currentPayStatus ?: PayStatus.NOT_STARTED
//    Log.d("PAY", "currentPayStatus: $currentPayStatus")

    var paymentStatus by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var shouldReload by remember { mutableStateOf(false) }

    LaunchedEffect(shouldReload) {
        if (shouldReload) {
            delay(3000)
            viewModel.fetchGiftBundleDetail(bundleId) // ✅ 다시 불러오기
            shouldReload = false
        }
    }

    fun startIamportPayment() {
        // 잏회용
        val paymentManager = PaymentManager(context, coroutineScope) { statusMessage ->
            paymentStatus = statusMessage
        }

        val appContext = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            PaymentApiEntryPoint::class.java
        )
        val paymentApi = entryPoint.paymentApiService()

        paymentManager.startIamportPayment(
            giftBundleId,
            productName,
            amount.toString(),
            buyerName,
            buyerTel,
            buyerEmail
        ) { impUid ->
            paymentManager.verifyPaymentOnServer(
                giftBundleId,
                impUid,
                paymentApi
            )
            dialogMessage = "결제가 완료되었습니다 🎉"
            showDialog = true
            shouldReload = true
        }
    }

    fun cancelIamportPayment() {

        val appContext = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            PaymentApiEntryPoint::class.java
        )
        val paymentApi = entryPoint.paymentApiService()

        coroutineScope.launch {
            try {
                val response = paymentApi.cancel(giftBundleId)
                if (response.isSuccessful) {
                    // 취소 성공 처리
                    Log.d("CANCEL", "결제 취소 성공")

                    if (response.isSuccessful) {
                        Log.d("CANCEL", "결제 취소 성공")
                        dialogMessage = "결제가 취소되었습니다 ❌"
                        showDialog = true
                        shouldReload = true
                    }
                } else {
                    // 실패 응답 처리
                    Log.w("CANCEL", "결제 취소 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CANCEL", "결제 취소 중 오류 발생", e)
            }
        }
    }

    LaunchedEffect(bundleId) {
        viewModel.fetchGiftBundleDetail(bundleId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearGiftBundleDetail()
        }
    }

    if (showDialog) {
        CustomResultDialog(
            message = dialogMessage,
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "선물 꾸러미 정보",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.white)
            )
        },
        bottomBar = {
            if (giftBundleDetail != null) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 60.dp,
                        top = 12.dp
                    )
                ) {
                    Button(

                        onClick = {
                            if (currentPayStatus.equals(PayStatus.PAID)) {
                                cancelIamportPayment()
                            } else {
                                startIamportPayment()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (currentPayStatus.equals(PayStatus.PAID)) {
                            Text("취소하기", style = MaterialTheme.typography.bodyLarge)
                        } else {
                            Text("결제하기", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        },
        containerColor = AppColor.background
    ) { innerPadding ->
        if (giftBundleDetail == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val bundle = giftBundleDetail!!
            val totalPrice = bundle.products.sumOf { it.price }
            val formatter = DecimalFormat("#,###")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "'${bundle.receiverNickname ?: "-" }' 님을 위한",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.clickable {
                            navController.navigate("myContactDetail/${bundle.receiverInfoId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "'${bundle.eventName ?: "-"}' 을 위한 꾸러미",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("eventDetail/${bundle.eventId}")
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "포함된 선물 목록",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(bundle.products) { product ->
                    ProductItem(
                        product = product,
                        onClick = {
                            if (product.detailPath.isNotBlank()) {
                                val encodedUrl = URLEncoder.encode(product.detailPath, "UTF-8")
                                navController.navigate("webView?url=$encodedUrl")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "총 ${formatter.format(totalPrice)}원",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppColor.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: GiftBundleResponse.Product,
    onClick: () -> Unit
) {
    val isClickable = product.detailPath.isNotBlank()
    val formatter = DecimalFormat("#,###")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isClickable) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.white)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = product.imagePath,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
            }
            Text(
                text = "${formatter.format(product.price)}원",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
