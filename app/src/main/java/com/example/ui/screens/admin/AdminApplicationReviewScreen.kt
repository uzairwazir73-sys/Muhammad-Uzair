package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.screens.user.ReviewRow
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminApplicationReviewScreen(
    adminUser: UserEntity?,
    application: ApplicationEntity,
    onUpdateStatus: (newStatus: String, feedbackNote: String, internalNote: String) -> Unit,
    onBackClick: () -> Unit
) {
    var feedbackNote by remember { mutableStateOf(application.statusNote) }
    var internalNote by remember { mutableStateOf(application.internalAdminNote) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_review_screen")
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Review: ${application.applicationNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = BrandPrimary
                        )
                    )
                    Text(
                        text = "${application.applicantFullName} • ${application.opportunityTitle}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        maxLines = 1
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Current Review Status", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(BrandPrimaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = application.status.replace('_', ' '),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandPrimary)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Administrative Actions & Decision:", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                onUpdateStatus("APPROVED", "Your application has been verified and officially approved.", internalNote)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onUpdateStatus("CORRECTION_REQUIRED", feedbackNote.ifBlank { "Please upload a clearer copy of your CNIC / Transcript." }, internalNote)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusWarning),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Correction", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onUpdateStatus("REJECTED", feedbackNote.ifBlank { "Application did not meet minimum eligibility criteria." }, internalNote)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusError),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reject", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Applicant Personal Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Applicant Dossier", fontWeight = FontWeight.Bold, color = BrandPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    ReviewRow("Full Name", application.applicantFullName)
                    ReviewRow("Father Name", application.applicantFatherName)
                    ReviewRow("CNIC", application.applicantCnic)
                    ReviewRow("Mobile", application.applicantPhone)
                    ReviewRow("Email", application.applicantEmail)
                    ReviewRow("Gender", application.applicantGender)
                    ReviewRow("Date of Birth", application.applicantDob)
                    ReviewRow("Domicile / District", "${application.applicantDistrict}, ${application.applicantProvince}")
                    ReviewRow("Address", application.applicantAddress)
                }
            }

            // Academic Qualifications Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Academic & Work Credentials", fontWeight = FontWeight.Bold, color = BrandPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    ReviewRow("Qualification", application.qualification)
                    ReviewRow("Degree Title", application.degree)
                    ReviewRow("Board / University", application.institute)
                    ReviewRow("Marks / CGPA", application.marks)
                    ReviewRow("Passing Year", application.passingYear)
                    if (application.experienceDetails.isNotBlank()) {
                        ReviewRow("Experience / Skills", application.experienceDetails)
                    }
                }
            }

            // Documents Verification Checklist
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Attached Documents & Verification", fontWeight = FontWeight.Bold, color = BrandPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    application.attachedDocumentsList.split(",").forEach { doc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(doc.trim(), fontSize = 12.sp, color = TextPrimary)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StatusSuccessContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("VERIFIED", color = StatusSuccess, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Notes Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Official Feedback & Internal Notes", fontWeight = FontWeight.Bold, color = BrandPrimary)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = feedbackNote,
                        onValueChange = { feedbackNote = it },
                        label = { Text("Feedback Message (Visible to Citizen)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = internalNote,
                        onValueChange = { internalNote = it },
                        label = { Text("Internal Administrative Confidential Note") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onUpdateStatus(application.status, feedbackNote, internalNote)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Save Notes")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
