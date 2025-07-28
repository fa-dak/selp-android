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
    var currentText by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        currentText = ""
        for (char in message) {
            currentText += char
            delay(20L)
        }
        delay(300L)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColor.white, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = currentText,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
