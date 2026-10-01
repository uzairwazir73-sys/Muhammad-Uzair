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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ActivityLogEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandPrimaryDark
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
fun AdminDashboardScreen(
    adminUser: UserEntity?,
    totalUsers: Int,
    totalApplications: Int,
    pendingApplications: Int,
    approvedApplications: Int,
    totalOpportunities: Int,
    activeOpportunities: Int,
    recentLogs: List<ActivityLogEntity>,
    onNavigateOpportunities: () -> Unit,
    onNavigateApplications: () -> Unit,
    onNavigateUsers: () -> Unit,
    onNavigateContent: () -> Unit,
    onNavigateLogs: () -> Unit,
    onNavigateSettings: () -> Unit,
    onAddNewOpportunity: () -> Unit,
    onExportCsv: () -> Unit,
    onLogout: () -> Unit,
    onExitAdmin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_dashboard_screen")
    ) {
        AdminTopBar(
            title = "UZair Gfx Admin",
            adminUser = adminUser,
            onBackClick = onExitAdmin,
            onLogoutClick = onLogout
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Greeting
            item {
                Column {
                    Text(
                        text = "Good day, ${adminUser?.fullName ?: "Administrator"}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "UZair Gfx Commercial Administration Panel — Pakistan Opportunity Hub",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            // Quick Stats 2x2 Grids
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Total Users",
                            value = "$totalUsers",
                            trend = "+100% active",
                            icon = Icons.Default.Group,
                            bgColor = BrandPrimaryContainer,
                            accentColor = BrandPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Total Applications",
                            value = "$totalApplications",
                            trend = "UGX Submissions",
                            icon = Icons.Default.Assignment,
                            bgColor = StatusInfoContainer,
                            accentColor = StatusInfo,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Pending Review",
                            value = "$pendingApplications",
                            trend = "Awaiting decision",
                            icon = Icons.Default.PendingActions,
                            bgColor = StatusWarningContainer,
                            accentColor = StatusWarning,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Approved",
                            value = "$approvedApplications",
                            trend = "Verified passed",
                            icon = Icons.Default.TaskAlt,
                            bgColor = StatusSuccessContainer,
                            accentColor = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "All Opportunities",
                            value = "$totalOpportunities",
                            trend = "In database",
                            icon = Icons.Default.Article,
                            bgColor = BrandSurfaceVariant.copy(alpha = 0.5f),
                            accentColor = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Active / Published",
                            value = "$activeOpportunities",
                            trend = "Live on Portal",
                            icon = Icons.Default.Policy,
                            bgColor = StatusSuccessContainer,
                            accentColor = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Admin Actions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Admin Quick Actions",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onAddNewOpportunity,
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("admin_add_opportunity_btn")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Listing", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onExportCsv,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("admin_export_csv_btn")
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export CSV", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Management Navigation Hub
            item {
                Text(
                    text = "System Administration Hub",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AdminNavTile(
                        title = "Manage Opportunities & Gazettes",
                        subtitle = "Add, edit, duplicate, verify sources for jobs, universities & scholarships",
                        icon = Icons.Default.Work,
                        onClick = onNavigateOpportunities
                    )
                    AdminNavTile(
                        title = "Applications Review Queue",
                        subtitle = "Inspect applicant submissions, attached documents & set status",
                        icon = Icons.Default.Assignment,
                        badgeText = if (pendingApplications > 0) "$pendingApplications Pending" else null,
                        onClick = onNavigateApplications
                    )
                    AdminNavTile(
                        title = "User Accounts & Security",
                        subtitle = "View registered citizens, block/unblock, and grant admin roles",
                        icon = Icons.Default.Group,
                        onClick = onNavigateUsers
                    )
                    AdminNavTile(
                        title = "Content, Banners & Announcements",
                        subtitle = "Manage urgent alerts, FAQ answers & broadcast push notifications",
                        icon = Icons.Default.Campaign,
                        onClick = onNavigateContent
                    )
                    AdminNavTile(
                        title = "Administrative Audit Logs",
                        subtitle = "Tamper-evident trail of administrative decisions and status changes",
                        icon = Icons.Default.History,
                        onClick = onNavigateLogs
                    )
                    AdminNavTile(
                        title = "Portal Configuration & Settings",
                        subtitle = "Brand colors, contact helpline, WhatsApp desk & backup",
                        icon = Icons.Default.Settings,
                        onClick = onNavigateSettings
                    )
                }
            }

            // Recent Audit Logs preview
            if (recentLogs.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Audit Activities",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "View All",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BrandPrimary, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.clickable { onNavigateLogs() }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            recentLogs.take(4).forEach { log ->
                                val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(log.timestamp))
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(BrandPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                        Text(text = "${log.adminName} — ${log.targetRecord}", fontSize = 11.sp, color = TextSecondary)
                                    }
                                    Text(text = dateStr, fontSize = 10.sp, color = TextMuted)
                                }
                                HorizontalDivider(color = BrandSurfaceVariant.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    trend: String,
    icon: ImageVector,
    bgColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = trend,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun AdminNavTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BrandPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    badgeText?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(StatusWarningContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StatusWarning,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
