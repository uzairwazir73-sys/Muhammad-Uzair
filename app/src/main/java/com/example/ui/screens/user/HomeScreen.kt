package com.example.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AnnouncementEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.components.BrandLogo
import com.example.ui.components.OfficialDisclaimerCard
import com.example.ui.components.OpportunityCard
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.concurrent.TimeUnit

data class CategoryQuickItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color,
    val bgColor: Color
)

@Composable
fun HomeScreen(
    currentUser: UserEntity?,
    featuredOpportunities: List<OpportunityEntity>,
    latestOpportunities: List<OpportunityEntity>,
    announcements: List<AnnouncementEntity>,
    savedIds: List<Long>,
    onCategoryClick: (String) -> Unit,
    onOpportunityClick: (Long) -> Unit,
    onSaveClick: (Long) -> Unit,
    onApplyClick: (Long) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onExploreAll: () -> Unit,
    onOnlineServicesClick: () -> Unit,
    onFaqClick: () -> Unit
) {
    var queryText by remember { mutableStateOf("") }

    val categories = listOf(
        CategoryQuickItem("GOV_JOB", "Govt Jobs", "FPSC, PPSC, Federal", Icons.Default.Work, Color(0xFF0284C7), Color(0xFFE0F2FE)),
        CategoryQuickItem("UNIVERSITY", "Universities", "BS, MS, NET, ECAT", Icons.Default.School, Color(0xFF7C3AED), Color(0xFFEDE9FE)),
        CategoryQuickItem("SCHOLARSHIP", "Scholarships", "HEC, Fully Funded", Icons.Default.CardMembership, Color(0xFF059669), Color(0xFFD1FAE5)),
        CategoryQuickItem("COLLEGE", "Colleges", "FSc, ICS, FA, ICom", Icons.Default.AccountBalance, Color(0xFFD97706), Color(0xFFFEF3C7)),
        CategoryQuickItem("PRIVATE_JOB", "Private Jobs", "IT, Engineering, Bank", Icons.Outlined.WorkOutline, Color(0xFF2563EB), Color(0xFFDBEAFE)),
        CategoryQuickItem("HAJJ", "Hajj 2026", "Govt Scheme, Quotas", Icons.Default.Mosque, Color(0xFF0D9488), Color(0xFFCCFBF1)),
        CategoryQuickItem("UMRAH", "Umrah Portal", "Visas, Nusuk, Packages", Icons.Default.Flight, Color(0xFF4F46E5), Color(0xFFE0E7FF)),
        CategoryQuickItem("ONLINE_SERVICE", "Online Services", "Apply & Form Filing", Icons.Default.SupportAgent, Color(0xFFE11D48), Color(0xFFFFE4E6))
    )

    // Deadline alerts (under 7 days remaining)
    val now = System.currentTimeMillis()
    val closingSoon = latestOpportunities.filter { opp ->
        val diffMs = opp.deadlineTimestamp - now
        val days = TimeUnit.MILLISECONDS.toDays(diffMs)
        days in 0..7
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(BrandPrimary, BrandPrimaryDark)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (currentUser != null) "Welcome, ${currentUser.fullName.split(" ").firstOrNull() ?: "Applicant"}" else "Welcome to UZair Gfx",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Discover Verified Jobs, Admissions & Scholarships in Pakistan",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            )
                        }
                        BrandLogo(size = 46.dp, isCircle = true)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero Search Box
                    OutlinedTextField(
                        value = queryText,
                        onValueChange = { queryText = it },
                        placeholder = {
                            Text(
                                "Search FPSC, NUST, HEC, Lecturer, Hajj...",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = BrandPrimary)
                        },
                        trailingIcon = {
                            if (queryText.isNotBlank()) {
                                IconButton(onClick = {
                                    onSearchSubmit(queryText)
                                }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Search", tint = BrandPrimary)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BrandSurface,
                            unfocusedContainerColor = BrandSurface,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hero_search_input")
                    )
                }
            }
        }

        // Quick Category Grid (2 rows x 4 items horizontal scroll or cards)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explore Portals & Categories",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    TextButton(onClick = onExploreAll) {
                        Text("View All", color = BrandPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2x4 Categories Grid
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (i in 0..1) {
                            val cat = categories[i]
                            CategoryQuickCard(
                                item = cat,
                                onClick = { onCategoryClick(cat.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (i in 2..3) {
                            val cat = categories[i]
                            CategoryQuickCard(
                                item = cat,
                                onClick = { onCategoryClick(cat.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (i in 4..5) {
                            val cat = categories[i]
                            CategoryQuickCard(
                                item = cat,
                                onClick = { onCategoryClick(cat.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (i in 6..7) {
                            val cat = categories[i]
                            CategoryQuickCard(
                                item = cat,
                                onClick = {
                                    if (cat.id == "ONLINE_SERVICE") onOnlineServicesClick()
                                    else onCategoryClick(cat.id)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Official Disclaimer Notice (Requirement 63)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                OfficialDisclaimerCard()
            }
        }

        // Urgent Announcements
        if (announcements.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = LogoRedAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Important Announcements",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    announcements.take(2).forEach { ann ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = BrandSurface)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (ann.isUrgent) LogoRedAccent else BrandPrimary)
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = ann.badgeText,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ann.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = ann.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Deadline Alerts ("Closing Soon")
        if (closingSoon.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = StatusWarning,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Deadline Alerts (Closing Soon)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        closingSoon.take(4).forEach { opp ->
                            val diff = TimeUnit.MILLISECONDS.toDays(opp.deadlineTimestamp - now)
                            Card(
                                modifier = Modifier
                                    .width(260.dp)
                                    .clickable { onOpportunityClick(opp.id) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = BrandSurface)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(StatusWarningContainer)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (diff == 0L) "DEADLINE TODAY" else "$diff DAYS REMAINING",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = StatusWarning,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = opp.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = opp.organization,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Featured Opportunities
        if (featuredOpportunities.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Text(
                        text = "Featured Opportunities",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(featuredOpportunities.take(3)) { opp ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
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

        // Online Assistance Desk Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { onOnlineServicesClick() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Need Online Application Assistance?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "We help file forms, verify challans, prepare CVs & compress documents without mistakes.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }
            }
        }

        // Latest Opportunities Header
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Latest Published Listings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    TextButton(onClick = onExploreAll) {
                        Text("See All", color = BrandPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Latest Opportunities List
        items(latestOpportunities.take(5)) { opp ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                OpportunityCard(
                    opportunity = opp,
                    isSaved = savedIds.contains(opp.id),
                    onCardClick = { onOpportunityClick(opp.id) },
                    onSaveClick = { onSaveClick(opp.id) },
                    onApplyClick = { onApplyClick(opp.id) }
                )
            }
        }

        // FAQ & Help link
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = onFaqClick,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Frequently Asked Questions & Contact Support")
                }
            }
        }
    }
}

@Composable
fun CategoryQuickCard(
    item: CategoryQuickItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("quick_category_${item.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(item.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
