package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FilterState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    initialState: FilterState,
    onApply: (FilterState) -> Unit,
    onDismiss: () -> Unit
) {
    var state by remember { mutableStateOf(initialState) }

    val provinces = listOf("ALL", "Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan", "Federal")
    val fundingTypes = listOf("ALL", "Fully Funded", "Partial", "Self-Finance")
    val genders = listOf("ALL", "Male", "Female")
    val sorts = listOf("NEWEST" to "Newest First", "DEADLINE" to "Deadline Approaching", "A-Z" to "Alphabetical (A-Z)")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = BrandSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Refine Opportunities",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sort By
                Text(
                    text = "Sort Results By",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    sorts.forEach { (key, label) ->
                        FilterChip(
                            label = label,
                            selected = state.sortBy == key,
                            onClick = { state = state.copy(sortBy = key) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Province
                Text(
                    text = "Province / Region",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    provinces.forEach { prov ->
                        FilterChip(
                            label = if (prov == "ALL") "All Regions" else prov,
                            selected = state.province == prov,
                            onClick = { state = state.copy(province = prov) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Funding
                Text(
                    text = "Scholarship / Funding Type",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fundingTypes.forEach { fund ->
                        FilterChip(
                            label = if (fund == "ALL") "All Funding" else fund,
                            selected = state.fundingType == fund,
                            onClick = { state = state.copy(fundingType = fund) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Gender
                Text(
                    text = "Gender Eligibility",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    genders.forEach { gen ->
                        FilterChip(
                            label = if (gen == "ALL") "Any" else gen,
                            selected = state.gender == gen,
                            onClick = { state = state.copy(gender = gen) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { state = FilterState(category = state.category) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            onApply(state)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("apply_filter_button")
                    ) {
                        Text("Apply Filters")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) BrandPrimary else BrandSurfaceVariant.copy(alpha = 0.6f))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color.White else TextPrimary,
                fontSize = 11.5.sp
            )
        )
    }
}
