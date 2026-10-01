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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.ApplicationEntity
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
fun MyApplicationsScreen(
    applications: List<ApplicationEntity>,
    onExploreClick: () -> Unit
) {
    var selectedApp by remember { mutableStateOf<ApplicationEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("my_applications_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "My Applications & Tracking",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Real-time timeline and administrative review updates",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        if (applications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Applications Submitted Yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You haven't submitted any admission or job applications through UZair Gfx portal yet.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text("Discover Opportunities")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(applications) { app ->
                    ApplicationCard(
                        application = app,
                        onClick = { selectedApp = app }
                    )
                }
            }
        }
    }

    // Detail Dialog
    selectedApp?.let { app ->
        ApplicationDetailDialog(
            application = app,
            onDismiss = { selectedApp = null }
        )
    }
}

@Composable
fun ApplicationCard(
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
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("app_item_${application.applicationNumber}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = application.applicationNumber,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = BrandPrimary,
                        letterSpacing = 1.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = application.status.replace('_', ' '),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = statusFg,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = application.opportunityTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                ),
                maxLines = 2
            )

            Text(
                text = application.organization,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Timeline tracker representation
            ApplicationTimelineRow(status = application.status)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Submitted: $dateStr",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                )

                Text(
                    text = "View Slip & Feedback →",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun ApplicationTimelineRow(status: String) {
    val stepIndex = when (status) {
        "SUBMITTED" -> 1
        "UNDER_REVIEW" -> 2
        "CORRECTION_REQUIRED" -> 2
        "APPROVED" -> 3
        "COMPLETED" -> 4
        "REJECTED" -> 3
        else -> 1
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimelineNode(step = 1, currentStep = stepIndex, label = "Submitted")
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 2.dp), color = if (stepIndex >= 2) BrandPrimary else BrandSurfaceVariant)
        TimelineNode(step = 2, currentStep = stepIndex, label = "Under Review")
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 2.dp), color = if (stepIndex >= 3) BrandPrimary else BrandSurfaceVariant)
        TimelineNode(
            step = 3,
            currentStep = stepIndex,
            label = if (status == "REJECTED") "Rejected" else "Approved",
            isError = status == "REJECTED"
        )
    }
}

@Composable
fun TimelineNode(step: Int, currentStep: Int, label: String, isError: Boolean = false) {
    val isDone = currentStep >= step
    val color = if (isError && isDone) StatusError else if (isDone) BrandPrimary else BrandSurfaceVariant
    val textCol = if (isDone) TextPrimary else TextMuted

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                color = textCol
            )
        )
    }
}

@Composable
fun ApplicationDetailDialog(
    application: ApplicationEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BrandSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Application Tracking Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "ID: ${application.applicationNumber}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = application.opportunityTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = application.organization,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BrandSurfaceVariant)

                // Status note / admin feedback
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BrandPrimaryContainer.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Current Status: ${application.status}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandPrimary)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = application.statusNote.ifBlank { "No additional notes provided." },
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontSize = 11.5.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Applicant Data Snapshot", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                ReviewRow("Applicant Name", application.applicantFullName)
                ReviewRow("Father Name", application.applicantFatherName)
                ReviewRow("CNIC", application.applicantCnic)
                ReviewRow("Phone", application.applicantPhone)
                ReviewRow("Email", application.applicantEmail)
                ReviewRow("Education", application.degree)
                ReviewRow("Attached Documents", application.attachedDocumentsList)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text("Close")
                }
            }
        }
    }
}
