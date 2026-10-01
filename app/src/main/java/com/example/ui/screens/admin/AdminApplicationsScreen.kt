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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorContainer
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusInfoContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminApplicationsScreen(
    adminUser: UserEntity?,
    applications: List<ApplicationEntity>,
    onReviewApplication: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val statusFilters = listOf("ALL", "SUBMITTED", "UNDER_REVIEW", "CORRECTION_REQUIRED", "APPROVED", "REJECTED", "COMPLETED")

    val filtered = applications.filter { app ->
        val currentFilter = statusFilters[selectedTab]
        val matchesStatus = currentFilter == "ALL" || app.status == currentFilter
        val matchesSearch = searchQuery.isBlank() ||
            app.applicationNumber.contains(searchQuery, ignoreCase = true) ||
            app.applicantFullName.contains(searchQuery, ignoreCase = true) ||
            app.applicantCnic.contains(searchQuery, ignoreCase = true) ||
            app.opportunityTitle.contains(searchQuery, ignoreCase = true)

        matchesStatus && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_applications_screen")
    ) {
        AdminTopBar(
            title = "Applications Queue",
            adminUser = adminUser,
            onBackClick = onBackClick,
            onLogoutClick = onBackClick
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by UGX ID, Applicant name, CNIC...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = BrandSurface,
                    contentColor = BrandPrimary,
                    edgePadding = 0.dp
                ) {
                    statusFilters.forEachIndexed { index, st ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = st.replace('_', ' '),
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${filtered.size} Applications In View",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextSecondary)
            )
        }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(48.dp), tint = TextMuted)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No applications matching criteria", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { app ->
                    AdminApplicationQueueCard(
                        application = app,
                        onClick = { onReviewApplication(app.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminApplicationQueueCard(
    application: ApplicationEntity,
    onClick: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(application.submissionDate))

    val (statusBg, statusFg) = when (application.status) {
        "APPROVED" -> Pair(StatusSuccessContainer, StatusSuccess)
        "REJECTED" -> Pair(StatusErrorContainer, StatusError)
        "CORRECTION_REQUIRED" -> Pair(StatusWarningContainer, LogoRedAccent)
        "UNDER_REVIEW" -> Pair(StatusWarningContainer, StatusWarning)
        "COMPLETED" -> Pair(StatusSuccessContainer, BrandPrimary)
        else -> Pair(StatusInfoContainer, StatusInfo)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("admin_app_card_${application.applicationNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = application.applicationNumber,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = BrandPrimary
                        )
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(statusBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = application.status.replace('_', ' '),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = statusFg,
                                fontSize = 9.5.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${application.applicantFullName} (CNIC: ${application.applicantCnic})",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "${application.opportunityTitle} • ${application.organization}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Applied on: $dateStr • ${application.applicantPhone}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}
