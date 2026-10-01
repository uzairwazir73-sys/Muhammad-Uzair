package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class CategoryItem(val id: String, val label: String)

val ALL_CATEGORIES = listOf(
    CategoryItem("ALL", "All Opportunities"),
    CategoryItem("GOV_JOB", "Govt Jobs"),
    CategoryItem("UNIVERSITY", "Universities"),
    CategoryItem("SCHOLARSHIP", "Scholarships"),
    CategoryItem("COLLEGE", "Colleges"),
    CategoryItem("PRIVATE_JOB", "Private Jobs"),
    CategoryItem("HAJJ", "Hajj 2026"),
    CategoryItem("UMRAH", "Umrah Services"),
    CategoryItem("ONLINE_SERVICE", "Online Services")
)

@Composable
fun CategoryChips(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ALL_CATEGORIES.forEach { item ->
            val isSelected = selectedCategory == item.id
            val bg = if (isSelected) BrandPrimary else BrandSurface
            val textCol = if (isSelected) Color.White else TextSecondary
            val borderModifier = if (isSelected) Modifier else Modifier.clip(RoundedCornerShape(20.dp)).background(BrandSurfaceVariant.copy(alpha = 0.5f))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bg)
                    .clickable { onCategorySelected(item.id) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("category_chip_${item.id}")
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textCol,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}
