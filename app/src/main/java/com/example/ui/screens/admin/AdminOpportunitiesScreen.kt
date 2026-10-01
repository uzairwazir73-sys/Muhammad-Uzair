package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.components.CategoryChips
import com.example.ui.components.formatCategory
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminOpportunitiesScreen(
    adminUser: UserEntity?,
    opportunities: List<OpportunityEntity>,
    onAddNew: () -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Long, String) -> Unit,
    onTogglePublish: (Long, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }

    val filtered = opportunities.filter { opp ->
        val matchesQuery = searchQuery.isBlank() ||
            opp.title.contains(searchQuery, ignoreCase = true) ||
            opp.organization.contains(searchQuery, ignoreCase = true)
        val matchesCat = selectedCategory == "ALL" || opp.category == selectedCategory
        matchesQuery && matchesCat
    }

    Scaffold(
        topBar = {
            AdminTopBar(
                title = "Manage Opportunities",
                adminUser = adminUser,
                onBackClick = onBackClick,
                onLogoutClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNew,
                containerColor = BrandPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("admin_fab_add_opp")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Opportunity")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BrandBackground)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BrandSurface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Filter opportunities by title or organization...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CategoryChips(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filtered.size} Listings Found",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextSecondary)
                )

                Text(
                    text = "+ Add New",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandPrimary),
                    modifier = Modifier.clickable { onAddNew() }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { opp ->
                    AdminOpportunityCard(
                        opportunity = opp,
                        onEdit = { onEdit(opp.id) },
                        onDelete = { onDelete(opp.id, opp.title) },
                        onTogglePublish = { onTogglePublish(opp.id, opp.status, opp.title) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminOpportunityCard(
    opportunity: OpportunityEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTogglePublish: () -> Unit
) {
    val isPublished = opportunity.status == "PUBLISHED"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(BrandPrimaryContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = formatCategory(opportunity.category),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary,
                                fontSize = 9.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isPublished) StatusSuccessContainer else StatusWarningContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = opportunity.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isPublished) StatusSuccess else BrandPrimary,
                                fontSize = 9.sp
                            )
                        )
                    }
                }

                Row {
                    IconButton(onClick = onTogglePublish, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (isPublished) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Publish",
                            tint = if (isPublished) StatusSuccess else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BrandPrimary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LogoRedAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = opportunity.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${opportunity.organization} • ${opportunity.city}, ${opportunity.province}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Deadline: ${opportunity.lastDate}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                )
                Text(
                    text = "${opportunity.totalApplicationsCount} Applicants",
                    style = MaterialTheme.typography.labelSmall.copy(color = BrandPrimary, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
