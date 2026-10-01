package com.example.ui.screens.user

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.NotificationEntity
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
    notifications: List<NotificationEntity>,
    onNotificationClick: (NotificationEntity) -> Unit,
    onMarkAllRead: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("notifications_screen")
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Notification Center",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Deadlines, admissions, job announcements",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }

                if (notifications.any { !it.isRead }) {
                    TextButton(onClick = onMarkAllRead) {
                        Text("Mark all read", color = BrandPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Notifications",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You're all caught up! New job alerts, scholarship deadlines, and admission announcements will appear here.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { item ->
                    val dateFormatted = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(item.timestamp))
                    val icon = when (item.category) {
                        "GOV_JOB", "JOB" -> Icons.Default.Work
                        "UNIVERSITY", "ADMISSION" -> Icons.Default.School
                        "SCHOLARSHIP" -> Icons.Default.CardMembership
                        "APPLICATION_UPDATE" -> Icons.Default.Assignment
                        else -> Icons.Default.Campaign
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNotificationClick(item) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!item.isRead) Color.White else BrandSurface.copy(alpha = 0.8f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (!item.isRead) 2.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (!item.isRead) BrandPrimaryContainer else BrandBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (!item.isRead) BrandPrimary else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    )
                                    if (!item.isRead) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(LogoRedAccent)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = item.message,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = dateFormatted,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
