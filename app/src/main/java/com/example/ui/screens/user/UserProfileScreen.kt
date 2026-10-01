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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.components.BrandLogo
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UserProfileScreen(
    user: UserEntity?,
    onUpdateProfile: (UserEntity) -> Unit,
    onLogout: () -> Unit,
    onLoginClick: () -> Unit
) {
    if (user == null) {
        // Logged-out state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BrandBackground)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BrandLogo(size = 64.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sign In to UZair Gfx",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Sign in with Google or your email to track your applications and auto-fill future forms.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onLoginClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("profile_signin_button")
                    ) {
                        Text("Sign In / Create Account", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    var fullName by remember(user) { mutableStateOf(user.fullName) }
    var fatherName by remember(user) { mutableStateOf(user.fatherName) }
    var cnic by remember(user) { mutableStateOf(user.cnic) }
    var phone by remember(user) { mutableStateOf(user.phone) }
    var dob by remember(user) { mutableStateOf(user.dob) }
    var gender by remember(user) { mutableStateOf(user.gender) }
    var province by remember(user) { mutableStateOf(user.province) }
    var district by remember(user) { mutableStateOf(user.district) }
    var address by remember(user) { mutableStateOf(user.address) }
    var educationLevel by remember(user) { mutableStateOf(user.educationLevel) }
    var skills by remember(user) { mutableStateOf(user.skills) }
    var experience by remember(user) { mutableStateOf(user.experience) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("user_profile_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Profile & Auto-Fill Data",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                OutlinedButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = StatusError,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Logout", color = StatusError, fontSize = 12.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Profile Card Top Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(BrandPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.fullName.take(1).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.fullName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (user.authProvider == "GOOGLE") StatusSuccessContainer else BrandPrimaryContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (user.authProvider == "GOOGLE") "Authenticated via Google" else "Email Verified Account",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (user.authProvider == "GOOGLE") StatusSuccess else BrandPrimary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Auto-fill Information Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auto-Fill Personal Information",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = "Information entered here will automatically populate your future applications.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = fatherName,
                        onValueChange = { fatherName = it },
                        label = { Text("Father's Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = cnic,
                        onValueChange = { cnic = it },
                        label = { Text("CNIC Number (37405-XXXXXXX-X)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Mobile Number") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            label = { Text("Date of Birth") },
                            placeholder = { Text("YYYY-MM-DD") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            label = { Text("Province") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("District") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Permanent Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    HorizontalDivider(color = BrandSurfaceVariant)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Academic & Professional Background",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = educationLevel,
                        onValueChange = { educationLevel = it },
                        label = { Text("Highest Qualification (e.g. Matric, FSc, BS, MS, PhD)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = skills,
                        onValueChange = { skills = it },
                        label = { Text("Key Skills (comma-separated)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = experience,
                        onValueChange = { experience = it },
                        label = { Text("Experience Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val updated = user.copy(
                                fullName = fullName,
                                fatherName = fatherName,
                                cnic = cnic,
                                phone = phone,
                                dob = dob,
                                gender = gender,
                                province = province,
                                district = district,
                                address = address,
                                educationLevel = educationLevel,
                                skills = skills,
                                experience = experience
                            )
                            onUpdateProfile(updated)
                        },
                        modifier = Modifier.fillMaxWidth().testTag("save_profile_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Profile Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
