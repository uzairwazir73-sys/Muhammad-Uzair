package com.example.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.entity.OpportunityEntity
import com.example.ui.components.OpportunityCard
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SavedOpportunitiesScreen(
    savedOpportunities: List<OpportunityEntity>,
    onOpportunityClick: (Long) -> Unit,
    onSaveToggle: (Long) -> Unit,
    onApplyClick: (Long) -> Unit,
    onExploreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("saved_opportunities_screen")
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Saved Bookmarks",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Opportunities and jobs bookmarked for later application",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        if (savedOpportunities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Saved Opportunities",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Click the bookmark icon on any university, job, or scholarship listing to save it here.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text("Explore Opportunities")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(savedOpportunities) { opp ->
                    OpportunityCard(
                        opportunity = opp,
                        isSaved = true,
                        onCardClick = { onOpportunityClick(opp.id) },
                        onSaveClick = { onSaveToggle(opp.id) },
                        onApplyClick = { onApplyClick(opp.id) }
                    )
                }
            }
        }
    }
}
