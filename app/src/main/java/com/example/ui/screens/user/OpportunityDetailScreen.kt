package com.example.ui.screens.user

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.OpportunityEntity
import com.example.ui.components.OfficialDisclaimerCard
import com.example.ui.components.formatCategory
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.concurrent.TimeUnit

@Composable
fun OpportunityDetailScreen(
    opportunity: OpportunityEntity,
    isSaved: Boolean,
    onBackClick: () -> Unit,
    onSaveToggle: () -> Unit,
    onApplyClick: () -> Unit
) {
    val context = LocalContext.current
    val now = System.currentTimeMillis()
    val diffMs = opportunity.deadlineTimestamp - now
    val daysLeft = TimeUnit.MILLISECONDS.toDays(diffMs).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("opportunity_detail_screen")
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandPrimary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out ${opportunity.title} at ${opportunity.organization} on UZair Gfx Portal!")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Opportunity"))
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = TextSecondary)
                    }

                    IconButton(onClick = onSaveToggle) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) LogoRedAccent else TextSecondary
                        )
                    }
                }
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Category & Verified Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(BrandPrimaryContainer)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = formatCategory(opportunity.category).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary,
                            fontSize = 11.sp
                        )
                    )
                }

                if (opportunity.isVerifiedSource) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusSuccessContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "VERIFIED OFFICIAL SOURCE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccess,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = opportunity.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    lineHeight = 26.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Organization
            Text(
                text = opportunity.organization,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = BrandPrimaryDark,
                    fontWeight = FontWeight.SemiBold
                )
            )

            if (opportunity.department.isNotBlank()) {
                Text(
                    text = "Dept: ${opportunity.department}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Deadline banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (daysLeft <= 3) StatusWarningContainer else BrandPrimaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = if (daysLeft <= 3) StatusWarning else BrandPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Application Deadline",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = opportunity.lastDate,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (daysLeft <= 0) "Expired" else if (daysLeft == 0) "Closing Today" else "$daysLeft Days Left",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (daysLeft <= 3) LogoRedAccent else BrandPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Source & Verification Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BrandSurface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = StatusSuccess,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Official Gazette & Source Info",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Source: ${opportunity.sourceName}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Text(
                        text = "Verified on: ${opportunity.verificationDate}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    if (opportunity.officialWebsite.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(opportunity.officialWebsite))
                                context.startActivity(browserIntent)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Visit Official Organization Website")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Key Overview Specs Table
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BrandSurface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Key Details & Specifications",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailSpecRow("Location", "${opportunity.city}, ${opportunity.province}")
                    if (opportunity.bpsGrade.isNotBlank()) DetailSpecRow("BPS / Grade", opportunity.bpsGrade)
                    if (opportunity.vacancies > 0) DetailSpecRow("Available Vacancies/Seats", "${opportunity.vacancies}")
                    if (opportunity.degreeLevel.isNotBlank()) DetailSpecRow("Degree Level", opportunity.degreeLevel)
                    DetailSpecRow("Gender Eligibility", opportunity.genderRequirement)
                    DetailSpecRow("Age Limit", opportunity.ageLimit)
                    DetailSpecRow("Salary / Scale", opportunity.salary)
                    DetailSpecRow("Application Fee", opportunity.fee)
                    DetailSpecRow("Opening Date", opportunity.openingDate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            if (opportunity.description.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Description & Scope",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = opportunity.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Eligibility Criteria
            if (opportunity.eligibilityCriteria.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = BrandPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Eligibility Criteria",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = opportunity.eligibilityCriteria,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Required Documents
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BrandSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Required Documents Checklist",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    opportunity.requiredDocuments.split(",").forEach { doc ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = BrandPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = doc.trim(),
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Disclaimer
            OfficialDisclaimerCard()

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom Sticky Action Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Official Deadline",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                    Text(
                        text = opportunity.lastDate,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("detail_apply_now_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Apply Online Now", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun DetailSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        )
    }
    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), color = BrandSurfaceVariant.copy(alpha = 0.5f))
}
