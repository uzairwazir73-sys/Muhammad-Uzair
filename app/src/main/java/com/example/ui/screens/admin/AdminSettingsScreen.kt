package com.example.ui.screens.admin

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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.components.BrandLogo
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminSettingsScreen(
    adminUser: UserEntity?,
    onExportCsv: () -> String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var exportedCsvText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_settings_screen")
    ) {
        AdminTopBar(
            title = "Portal Settings",
            adminUser = adminUser,
            onBackClick = onBackClick,
            onLogoutClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand & Identity Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Brand & Portal Identity", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BrandLogo(size = 54.dp, showBorder = true)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Official Brand: UZair Gfx", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Tagline: Online Apply & Information Portal", color = TextSecondary, fontSize = 11.5.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(BrandPrimary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Primary #0E7490", fontSize = 11.sp, fontWeight = FontWeight.Medium)

                                Spacer(modifier = Modifier.width(12.dp))

                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(LogoRedAccent)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Accent Red", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            // Data Export & CSV Generation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Data Management & CSV Export", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Text(
                        text = "Export all registered citizen applications with timestamps and candidate credentials.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.5.sp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val csv = onExportCsv()
                            exportedCsvText = csv
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, csv)
                                putExtra(Intent.EXTRA_SUBJECT, "UZair Gfx Applications Export.csv")
                                type = "text/csv"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Export Applications CSV"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("export_csv_action_btn")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export Applications to CSV / Excel", fontWeight = FontWeight.Bold)
                    }

                    exportedCsvText?.let { csv ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandBackground)
                                .padding(10.dp)
                        ) {
                            Column {
                                Text("CSV Preview Generated:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BrandPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = csv.lines().take(4).joinToString("\n"),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // System Security & Environment
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = StatusSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Security & Access Controls", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Admin Authorization:", fontSize = 12.sp, color = TextSecondary)
                        Text("Role-Based RBAC Active", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = BrandSurfaceVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Local Persistence:", fontSize = 12.sp, color = TextSecondary)
                        Text("Room SQLite Database Encrypted", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = BrandSurfaceVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Password Cryptography:", fontSize = 12.sp, color = TextSecondary)
                        Text("SHA-256 Digest Hashing", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
