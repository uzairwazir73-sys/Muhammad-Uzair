package com.example.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.local.entity.OpportunityEntity
import com.example.ui.components.CategoryChips
import com.example.ui.components.FilterDialog
import com.example.ui.components.OpportunityCard
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FilterState

@Composable
fun ExploreScreen(
    opportunities: List<OpportunityEntity>,
    searchQuery: String,
    filterState: FilterState,
    savedIds: List<Long>,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onFilterApply: (FilterState) -> Unit,
    onOpportunityClick: (Long) -> Unit,
    onSaveClick: (Long) -> Unit,
    onApplyClick: (Long) -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    // Count non-default filters
    val activeFilterCount = (if (filterState.province != "ALL") 1 else 0) +
        (if (filterState.fundingType != "ALL") 1 else 0) +
        (if (filterState.gender != "ALL") 1 else 0) +
        (if (filterState.sortBy != "NEWEST") 1 else 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("explore_screen_root")
    ) {
        // Search & Filter header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = { Text("Search by title, university, department, city...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = BrandPrimary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = TextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BrandBackground,
                            unfocusedContainerColor = BrandBackground,
                            focusedBorderColor = BrandPrimary,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("explore_search_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Filter Button
                    BadgedBox(
                        badge = {
                            if (activeFilterCount > 0) {
                                Badge(
                                    containerColor = LogoRedAccent,
                                    contentColor = Color.White
                                ) {
                                    Text("$activeFilterCount")
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = { showFilterDialog = true },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (activeFilterCount > 0) BrandPrimaryContainer else BrandBackground)
                                .size(48.dp)
                                .testTag("open_filter_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = BrandPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Chips
                CategoryChips(
                    selectedCategory = filterState.category,
                    onCategorySelected = onCategorySelect
                )
            }
        }

        // Active results count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${opportunities.size} verified listing${if (opportunities.size != 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            )

            if (searchQuery.isNotBlank() || activeFilterCount > 0 || filterState.category != "ALL") {
                Text(
                    text = "Clear All Filters",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = LogoRedAccent,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // Listings or Empty State
        if (opportunities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Opportunities Found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try adjusting your search keywords, category, or region filters.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onSearchChange("")
                            onFilterApply(FilterState())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text("Reset Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(opportunities) { opp ->
                    OpportunityCard(
                        opportunity = opp,
                        isSaved = savedIds.contains(opp.id),
                        onCardClick = { onOpportunityClick(opp.id) },
                        onSaveClick = { onSaveClick(opp.id) },
                        onApplyClick = { onApplyClick(opp.id) }
                    )
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            initialState = filterState,
            onApply = onFilterApply,
            onDismiss = { showFilterDialog = false }
        )
    }
}
