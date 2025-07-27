package com.kosa.selp.features.gift.composable.bundle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.delay

@Composable
fun TypingMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        visibleText = ""
        for (i in message.indices) {
            visibleText = message.substring(0, i + 1)
            delay(10)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColor.white, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = visibleText,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}