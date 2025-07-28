package com.kosa.selp.features.survey.presentation.funnel


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kosa.selp.features.survey.presentation.state.SurveyEvent
import com.kosa.selp.features.survey.presentation.viewModel.SurveyViewModel
import com.kosa.selp.shared.theme.AppColor
import kotlinx.coroutines.launch

@Composable
fun AgeFunnel(
    viewModel: SurveyViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    val ages = listOf(
        "10" to "10대",
        "20" to "20대",
        "30" to "30대",
        "40" to "40대",
        "50" to "50대",
        "60" to "60대"
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "받는 분의 연령대는 어떻게 되나요?",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColor.textPrimary
        )

        Spacer(modifier = Modifier.height(48.dp))

        AgePickerRow(
            items = ages.map { it.first },
            selectedLabel = state.ageRange,
            onSelect = { idx ->
                val label = ages[idx].second
                viewModel.onEvent(SurveyEvent.AgeRangeSelected(label))
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onNext() },
            enabled = state.ageRange != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.primary,
                contentColor = AppColor.white
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("다음", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun AgePickerRow(
    items: List<String>,
    selectedLabel: String?,
    onSelect: (Int) -> Unit
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val itemDp = 64.dp
    val itemPx = with(density) { itemDp.toPx().toInt() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val centerOffset =
                (listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset) / 2
            val scrollOffset = 1 * itemPx - centerOffset + itemPx / 2
            listState.scrollToItem(1, scrollOffset)
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = itemDp / 2)
    ) {
        itemsIndexed(items) { idx, label ->
            val isSelected = selectedLabel == "${label}대"

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        coroutineScope.launch {
                            onSelect(idx)
                            val centerOffset =
                                (listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset) / 2
                            val scrollOffset = idx * itemPx - centerOffset + itemPx / 2
                            listState.animateScrollToItem(idx, scrollOffset)
                        }
                    }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) AppColor.primary.copy(alpha = 0.08f) else Color.Transparent,
                        modifier = Modifier
                            .size(56.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = label,
                                style = if (isSelected)
                                    MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AppColor.primary
                                    )
                                else
                                    MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = AppColor.textSecondary
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(AppColor.primary)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}
