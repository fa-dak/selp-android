package com.kosa.selp.shared.composable.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor

@Composable
fun BudgetInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var internalValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = formatWithCommas(value),
                selection = TextRange(formatWithCommas(value).length)
            )
        )
    }

    LaunchedEffect(Unit) {
        keyboardController?.show()
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        BasicTextField(
            value = internalValue,
            onValueChange = { textValue ->
                val raw = textValue.text.replace(",", "")
                if (raw.isEmpty()) {
                    internalValue = textValue.copy(selection = TextRange(0))
                    onValueChange("")
                } else if (raw.all { it.isDigit() }) {
                    val parsed = raw.toIntOrNull()
                    if (parsed != null && parsed <= 3_000_000) {
                        val formatted = formatWithCommas(raw)
                        internalValue = TextFieldValue(
                            text = formatted,
                            selection = TextRange(formatted.length)
                        )
                        onValueChange(raw)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = AppColor.textPrimary,
                textAlign = TextAlign.End
            ),
            cursorBrush = SolidColor(AppColor.primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = AppColor.textHint,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    innerTextField()
                }
            }
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = if (isError) MaterialTheme.colorScheme.error else AppColor.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        val amountOptions = listOf(
            "+10만원" to 100_000,
            "+30만원" to 300_000,
            "+50만원" to 500_000,
            "+100만원" to 1_000_000
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            amountOptions.forEach { (label, amount) ->
                AssistChip(
                    onClick = {
                        val current = value.replace(",", "").toIntOrNull() ?: 0
                        val newAmount = (current + amount).coerceAtMost(3_000_000)
                        val newRaw = newAmount.toString()
                        val formatted = formatWithCommas(newRaw)
                        internalValue = TextFieldValue(
                            text = formatted,
                            selection = TextRange(formatted.length)
                        )
                        onValueChange(newRaw)
                    },
                    label = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(label)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = AppColor.white,
                        labelColor = AppColor.textPrimary
                    )
                )
            }
        }
    }
}

fun formatWithCommas(number: String): String {
    return number.toLongOrNull()?.let {
        "%,d".format(it)
    } ?: number
}
