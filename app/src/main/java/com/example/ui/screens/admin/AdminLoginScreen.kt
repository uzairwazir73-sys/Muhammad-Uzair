package com.example.ui.screens.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandLogo
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminLoginScreen(
    onAdminLogin: (email: String, pass: String) -> Unit,
    onQuickSuperAdmin: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("uzaircomputer73@gmail.com") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_login_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("Back to User Portal", color = TextSecondary, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            BrandLogo(size = 72.dp, showBorder = true)

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "UZair Gfx Admin Portal",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary
                    )
                )
            }

            Text(
                text = "Secure Role-Based Management System",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Administrator Authorization",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Access is restricted to authorized administrative personnel only.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.5.sp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Admin Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BrandPrimary) },
                        modifier = Modifier.fillMaxWidth().testTag("admin_email_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Admin Password") },
                        placeholder = { Text("Leave blank if using Super Admin token") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandPrimary) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("admin_password_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                onAdminLogin(email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_signin_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Authenticate Admin", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = BrandSurfaceVariant)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Super Admin Authenticate button (for uzaircomputer73@gmail.com)
                    OutlinedButton(
                        onClick = onQuickSuperAdmin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("quick_super_admin_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "One-Tap Super Admin Access (uzaircomputer73)",
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
