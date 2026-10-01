package com.example.ui.screens.user

import android.content.Intent
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandLogo
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ApplicationReceiptScreen(
    applicationNumber: String,
    onTrackClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Icon
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(StatusSuccessContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = StatusSuccess,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Application Submitted Successfully!",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your submission has been registered and verified on the UZair Gfx Portal.",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Official Slip Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("official_application_receipt_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header of slip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrandLogo(size = 40.dp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "OFFICIAL RECEIPT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = currentDate,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = BrandSurfaceVariant)

                // Application ID Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandPrimaryContainer)
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "OFFICIAL APPLICATION NUMBER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = applicationNumber,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = BrandPrimary,
                                    letterSpacing = 2.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(applicationNumber))
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Initial Status:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusSuccessContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SUBMITTED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Verification:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Text("Verified Online Portal Submission", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = TextPrimary))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BrandSurfaceVariant)

                // Barcode simulation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .background(Color.White)
                        .border(1.dp, BrandSurfaceVariant, RoundedCornerShape(4.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "||| | ||||| || |||| ||| |||| | ||||| ||",
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Button(
            onClick = onTrackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("receipt_track_button"),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Timeline, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Track My Applications", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = {
                    val share = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "UZair Gfx Application Receipt: $applicationNumber submitted successfully!")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(share, "Share Application Receipt"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share")
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedButton(
                onClick = onHomeClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Home")
            }
        }
    }
}
