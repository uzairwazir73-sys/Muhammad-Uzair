package com.example.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.BrandLogo
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoDarkAccent
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AuthScreen(
    onGoogleLogin: (email: String, name: String) -> Unit,
    onEmailLogin: (email: String, pass: String) -> Unit,
    onEmailRegister: (name: String, email: String, pass: String, phone: String, cnic: String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Register
    var showGoogleDialog by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    // Form inputs
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    var regName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regCnic by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrandPrimary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Logo & Title
            BrandLogo(
                size = 72.dp,
                showBorder = true,
                modifier = Modifier.testTag("auth_logo")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "UZair Gfx",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
            )

            Text(
                text = "Online Apply & Information Portal",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // GOOGLE / GMAIL LOGIN BUTTON
            OutlinedButton(
                onClick = { showGoogleDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("continue_with_google_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = BrandSurface)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Google colored "G" badge
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, BrandSurfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = LogoRedAccent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Continue with Google",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider "OR"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = BrandSurfaceVariant)
                Text(
                    text = "  OR USE EMAIL  ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp
                    )
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = BrandSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card for Tabs and Fields
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Tab Switcher
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = BrandSurface,
                        contentColor = BrandPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = BrandPrimary
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Sign In", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Create Account", fontWeight = FontWeight.Bold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedTab == 0) {
                        // LOGIN TAB
                        OutlinedTextField(
                            value = loginEmail,
                            onValueChange = { loginEmail = it },
                            label = { Text("Email Address") },
                            placeholder = { Text("e.g. uzair@example.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = BrandPrimary)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_email_input"),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = { loginPassword = it },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = BrandPrimary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                                    Icon(
                                        imageVector = if (loginPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showForgotPasswordDialog = true }) {
                                Text(
                                    text = "Forgot Password?",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BrandPrimary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (loginEmail.isNotBlank() && loginPassword.isNotBlank()) {
                                    onEmailLogin(loginEmail, loginPassword)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("login_submit_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    } else {
                        // REGISTER TAB
                        OutlinedTextField(
                            value = regName,
                            onValueChange = { regName = it },
                            label = { Text("Full Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BrandPrimary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_name_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it },
                            label = { Text("Email Address *") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BrandPrimary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_email_input"),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = regPhone,
                            onValueChange = { regPhone = it },
                            label = { Text("Mobile Number (03XX-XXXXXXX)") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BrandPrimary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_phone_input"),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = regCnic,
                            onValueChange = { regCnic = it },
                            label = { Text("CNIC (Optional at signup)") },
                            placeholder = { Text("e.g. 37405-XXXXXXX-X") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_cnic_input"),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = regPassword,
                            onValueChange = { regPassword = it },
                            label = { Text("Create Password *") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandPrimary) },
                            trailingIcon = {
                                IconButton(onClick = { regPasswordVisible = !regPasswordVisible }) {
                                    Icon(
                                        imageVector = if (regPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (regPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_password_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                if (regName.isNotBlank() && regEmail.isNotBlank() && regPassword.isNotBlank()) {
                                    onEmailRegister(regName, regEmail, regPassword, regPhone, regCnic)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("reg_submit_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }

    // Google Account Picker Dialog
    if (showGoogleDialog) {
        Dialog(onDismissRequest = { showGoogleDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BrandSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BrandLogo(size = 36.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sign in with Google",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Choose a Google account to continue to UZair Gfx:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Account 1: uzaircomputer73@gmail.com
                    GoogleAccountItem(
                        name = "Uzair Computer",
                        email = "uzaircomputer73@gmail.com",
                        onClick = {
                            showGoogleDialog = false
                            onGoogleLogin("uzaircomputer73@gmail.com", "Uzair Computer")
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Account 2: Another standard account
                    GoogleAccountItem(
                        name = "Student Applicant",
                        email = "applicant.pak@gmail.com",
                        onClick = {
                            showGoogleDialog = false
                            onGoogleLogin("applicant.pak@gmail.com", "Student Applicant")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showGoogleDialog = false }) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        Dialog(onDismissRequest = { showForgotPasswordDialog = false }) {
            var resetEmail by remember { mutableStateOf("") }
            var submitted by remember { mutableStateOf(false) }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BrandSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Reset Your Password",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!submitted) {
                        Text(
                            text = "Enter your registered email address and we'll send you instructions to reset your password.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showForgotPasswordDialog = false }) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { submitted = true },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                            ) {
                                Text("Send Link")
                            }
                        }
                    } else {
                        Text(
                            text = "Password reset instructions have been dispatched to your email address.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showForgotPasswordDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleAccountItem(
    name: String,
    email: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BrandBackground)
            .clickable { onClick() }
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
            Text(
                text = name.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )
        }
    }
}
