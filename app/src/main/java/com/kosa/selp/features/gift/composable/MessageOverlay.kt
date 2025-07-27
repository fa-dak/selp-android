package com.kosa.selp.features.gift.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.composable.loading.DotsLoadingIndicator
import com.kosa.selp.shared.theme.AppColor

@Composable
fun MessageOverlay(
    messages: List<String>,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {})
                }
                .padding(horizontal = 24.dp, vertical = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            DotsLoadingIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {})
                }
                .padding(horizontal = 24.dp, vertical = 48.dp),
            contentAlignment = Alignment.Center,
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                items(messages) { message ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AppColor.white
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TypingMessage(
                                message = message,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(message))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "복사",
                                    tint = AppColor.primary
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.primary,
                            contentColor = AppColor.white
                        )
                    ) {
                        Text("닫기", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
