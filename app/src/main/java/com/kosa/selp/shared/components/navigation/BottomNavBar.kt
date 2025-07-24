package com.kosa.selp.shared.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kosa.selp.shared.theme.AppColor


@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf("홈", "선물", "일정","마이 페이지")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.CardGiftcard,
        Icons.Filled.DateRange,
        Icons.Filled.Face
    )

    NavigationBar(
        containerColor = AppColor.white,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, label ->
            val isSelected = selectedIndex == index

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        tint = if (isSelected) AppColor.primary else AppColor.textDisabled
                    )
                },

                label = {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) AppColor.primary else AppColor.textDisabled
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColor.primary,
                    unselectedIconColor = AppColor.textDisabled,
                    selectedTextColor = AppColor.primary,
                    unselectedTextColor = AppColor.textDisabled,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
