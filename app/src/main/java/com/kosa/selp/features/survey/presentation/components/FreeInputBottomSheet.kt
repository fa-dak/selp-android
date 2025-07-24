package com.kosa.selp.features.survey.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeInputBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                keyboardController?.hide()
                sheetState.hide()
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = AppColor.white
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "받는 분을 조금 더 자세히 설명해주세요.",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColor.textPrimary
            )

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        "ex. 요즘 향수에 관심이 많아요",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AppColor.textSecondary.copy(alpha = 0.5f)
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .focusRequester(focusRequester),
                singleLine = false,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = AppColor.divider,
                    focusedBorderColor = AppColor.primary,
                    cursorColor = AppColor.primary
                )
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    onSubmit(text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.primary,
                    contentColor = AppColor.white
                )
            ) {
                Text("완료", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
