package com.example.ui.screens.user

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.FaqEntity
import com.example.ui.components.OfficialDisclaimerCard
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FaqSupportScreen(
    faqs: List<FaqEntity>,
    onBackClick: () -> Unit,
    onSubmitTicket: (subject: String, message: String) -> Unit
) {
    val context = LocalContext.current
    var expandedFaqIndex by remember { mutableIntStateOf(-1) }

    var ticketSubject by remember { mutableStateOf("") }
    var ticketMessage by remember { mutableStateOf("") }
    var ticketSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("faq_support_screen")
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
                        text = "Help, FAQ & Support Desk",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Answers to common queries & official assistance",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OfficialDisclaimerCard()

            // Quick Contact Badges (WhatsApp & Email)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Direct Citizen Assistance Desk",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(StatusSuccessContainer.copy(alpha = 0.5f))
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/923001234567"))
                                context.startActivity(intent)
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("WhatsApp Support Helpline", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("+92 300 1234567 (Mon-Sat, 9AM-8PM)", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(BrandPrimaryContainer.copy(alpha = 0.5f))
                            .clickable {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:support@uzairgfx.portal")
                                }
                                context.startActivity(intent)
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BrandPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Official Support Email", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("support@uzairgfx.portal", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Frequently Asked Questions
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )

            faqs.forEachIndexed { index, faq ->
                val isExpanded = expandedFaqIndex == index
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { expandedFaqIndex = if (isExpanded) -1 else index },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = faq.question,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = BrandPrimary
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = BrandSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = faq.answer,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Support Ticket / Query Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Submit a Support Query",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Have a question about a gazette, challan, or application? Send us a message.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.5.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!ticketSubmitted) {
                        OutlinedTextField(
                            value = ticketSubject,
                            onValueChange = { ticketSubject = it },
                            label = { Text("Subject (e.g. Question regarding FPSC AD eligibility)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = ticketMessage,
                            onValueChange = { ticketMessage = it },
                            label = { Text("Describe your query or issue...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (ticketSubject.isNotBlank() && ticketMessage.isNotBlank()) {
                                    onSubmitTicket(ticketSubject, ticketMessage)
                                    ticketSubmitted = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send Message", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(StatusSuccessContainer)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Thank you! Your query has been logged. Our facilitation desk will reply within 24 hours.",
                                style = MaterialTheme.typography.bodySmall.copy(color = StatusSuccess, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
