package com.example.ui.screens.admin

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.local.entity.AnnouncementEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminContentScreen(
    adminUser: UserEntity?,
    announcements: List<AnnouncementEntity>,
    onCreateAnnouncement: (AnnouncementEntity) -> Unit,
    onDeleteAnnouncement: (Long) -> Unit,
    onBroadcastNotification: (title: String, message: String, category: String) -> Unit,
    onBackClick: () -> Unit
) {
    var notifTitle by remember { mutableStateOf("") }
    var notifMessage by remember { mutableStateOf("") }

    var annTitle by remember { mutableStateOf("") }
    var annDescription by remember { mutableStateOf("") }
    var annBadge by remember { mutableStateOf("Urgent") }
    var isUrgent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_content_screen")
    ) {
        AdminTopBar(
            title = "Content & Alerts",
            adminUser = adminUser,
            onBackClick = onBackClick,
            onLogoutClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Push Notification Broadcast Sender
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = BrandPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Broadcast Citizen Notification",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = "Send an immediate alert to all registered applicants.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.5.sp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = notifTitle,
                            onValueChange = { notifTitle = it },
                            label = { Text("Notification Title (e.g. FPSC Test Date Announced)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = notifMessage,
                            onValueChange = { notifMessage = it },
                            label = { Text("Notification Message") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (notifTitle.isNotBlank() && notifMessage.isNotBlank()) {
                                    onBroadcastNotification(notifTitle, notifMessage, "GENERAL")
                                    notifTitle = ""
                                    notifMessage = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.align(Alignment.End).testTag("admin_send_broadcast_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Broadcast Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Publish Announcement Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = LogoRedAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create Portal Announcement",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = annTitle,
                            onValueChange = { annTitle = it },
                            label = { Text("Announcement Headline") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = annDescription,
                            onValueChange = { annDescription = it },
                            label = { Text("Announcement Text Details") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = annBadge,
                                onValueChange = { annBadge = it },
                                label = { Text("Badge (e.g. Urgent, Closing Soon)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = isUrgent, onCheckedChange = { isUrgent = it })
                                Text("Urgent", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (annTitle.isNotBlank()) {
                                    val ann = AnnouncementEntity(
                                        title = annTitle,
                                        description = annDescription,
                                        badgeText = annBadge,
                                        isUrgent = isUrgent
                                    )
                                    onCreateAnnouncement(ann)
                                    annTitle = ""
                                    annDescription = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.align(Alignment.End).testTag("admin_publish_ann_btn")
                        ) {
                            Text("Publish Announcement", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Existing Announcements List
            item {
                Text(
                    text = "Live Announcements (${announcements.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(announcements) { ann ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (ann.isUrgent) LogoRedAccent else BrandPrimary)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(ann.badgeText, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(ann.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(ann.description, fontSize = 11.5.sp, color = TextSecondary)
                        }

                        IconButton(onClick = { onDeleteAnnouncement(ann.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LogoRedAccent)
                        }
                    }
                }
            }
        }
    }
}
