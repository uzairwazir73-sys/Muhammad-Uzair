package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.concurrent.TimeUnit

@Composable
fun OpportunityCard(
    opportunity: OpportunityEntity,
    isSaved: Boolean,
    onCardClick: () -> Unit,
    onSaveClick: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    // Calculate deadline status
    val now = System.currentTimeMillis()
    val diffMs = opportunity.deadlineTimestamp - now
    val daysLeft = TimeUnit.MILLISECONDS.toDays(diffMs).toInt()

    val (deadlineText, deadlineBg, deadlineFg) = when {
        diffMs <= 0 -> Triple("Expired", StatusErrorContainer, StatusError)
        daysLeft == 0 -> Triple("Deadline Today", StatusWarningContainer, StatusWarning)
        daysLeft == 1 -> Triple("1 Day Left", StatusWarningContainer, StatusWarning)
        daysLeft <= 3 -> Triple("$daysLeft Days Left", StatusWarningContainer, StatusWarning)
        else -> Triple("$daysLeft Days Left", BrandPrimaryContainer, BrandPrimary)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("opportunity_card_${opportunity.id}"),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category, Verified Badge, and Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Category Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BrandPrimaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = formatCategory(opportunity.category),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary,
                                fontSize = 10.sp
                            )
                        )
                    }

                    if (opportunity.isVerifiedSource) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StatusSuccessContainer)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Source",
                                    tint = StatusSuccess,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "VERIFIED",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StatusSuccess,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Bookmark Icon
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("bookmark_button_${opportunity.id}")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Save Opportunity",
                        tint = if (isSaved) LogoRedAccent else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = opportunity.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 15.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Organization
            Text(
                text = opportunity.organization,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.5.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Info items (Location / Qualification / Deadline)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${opportunity.city}, ${opportunity.province}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Deadline pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(deadlineBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = deadlineFg,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = deadlineText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = deadlineFg,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Last Date: ${opportunity.lastDate}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )

                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("apply_now_button_${opportunity.id}")
                ) {
                    Text(
                        text = "Apply Now",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

fun formatCategory(cat: String): String {
    return when (cat) {
        "UNIVERSITY" -> "University"
        "COLLEGE" -> "College"
        "GOV_JOB" -> "Govt Job"
        "PRIVATE_JOB" -> "Private Job"
        "SCHOLARSHIP" -> "Scholarship"
        "HAJJ" -> "Hajj Portal"
        "UMRAH" -> "Umrah Portal"
        "ONLINE_SERVICE" -> "Online Service"
        else -> cat.replace('_', ' ')
    }
}
