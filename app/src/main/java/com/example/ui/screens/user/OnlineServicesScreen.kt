package com.example.ui.screens.user

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.UserEntity
import com.example.ui.components.OfficialDisclaimerCard
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class ServiceOffering(
    val title: String,
    val description: String,
    val fee: String,
    val icon: ImageVector
)

@Composable
fun OnlineServicesScreen(
    currentUser: UserEntity?,
    onBackClick: () -> Unit,
    onSubmitRequest: (type: String, details: String, phone: String, name: String) -> Unit
) {
    val services = listOf(
        ServiceOffering(
            title = "Government & University Form Filing",
            description = "Complete assistance in filling FPSC, PPSC, NUST, ECAT, MDCAT, or HEC forms accurately with zero rejection risk.",
            fee = "PKR 500 - 1,000",
            icon = Icons.Default.Assignment
        ),
        ServiceOffering(
            title = "Professional CV & Resume Preparation",
            description = "ATS-compliant modern resume tailored for Pakistani corporate, banking, IT, or international scholarship applications.",
            fee = "PKR 800 - 1,500",
            icon = Icons.Default.Description
        ),
        ServiceOffering(
            title = "Photo & Document Formatting / Compression",
            description = "Exact dimensions, pixel specs (e.g. 300x350px, under 25KB) and blue/white backgrounds for official gazette portals.",
            fee = "PKR 200 - 400",
            icon = Icons.Default.Scanner
        ),
        ServiceOffering(
            title = "Challan Payment & Verification Support",
            description = "Assistance in generating PSID, fee voucher submission via JazzCash/Easypaisa/NBP, and roll number slip printing.",
            fee = "PKR 300",
            icon = Icons.Default.Print
        )
    )

    var selectedServiceForRequest by remember { mutableStateOf<ServiceOffering?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("online_services_screen")
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
                        text = "UZair Gfx Facilitation Services",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Expert Online Apply & Document Assistance Desk",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OfficialDisclaimerCard()

            Text(
                text = "Available Online Application Services",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )

            services.forEach { srv ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = srv.icon,
                                    contentDescription = null,
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = srv.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Estimated Fee: ${srv.fee}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = srv.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { selectedServiceForRequest = srv },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Request This Service", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }

    // Request Service Dialog
    selectedServiceForRequest?.let { srv ->
        var applicantName by remember { mutableStateOf(currentUser?.fullName ?: "") }
        var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
        var details by remember { mutableStateOf("") }
        var errorMsg by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { selectedServiceForRequest = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BrandSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Request Service",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        IconButton(onClick = { selectedServiceForRequest = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Text(
                        text = srv.title,
                        style = MaterialTheme.typography.labelMedium.copy(color = BrandPrimary, fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (errorMsg.isNotEmpty()) {
                        Text(text = errorMsg, color = Color.Red, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    OutlinedTextField(
                        value = applicantName,
                        onValueChange = { applicantName = it },
                        label = { Text("Your Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact WhatsApp / Mobile *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = details,
                        onValueChange = { details = it },
                        label = { Text("Details of your request (e.g. FPSC Roll number slip or NUST NET form)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (applicantName.isBlank()) {
                                errorMsg = "Please enter your name"
                            } else if (phone.isBlank()) {
                                errorMsg = "Please enter your phone number"
                            } else {
                                onSubmitRequest(srv.title, details, phone, applicantName)
                                selectedServiceForRequest = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit Service Request", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
