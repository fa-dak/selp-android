package com.kosa.selp.features.gift.presentation.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kosa.selp.MainActivity

@Composable
fun PayExampleScreen(activity: MainActivity) {
    Button(onClick = { activity.launchPayment() }) {
        Text("결제 테스트")
    }
}