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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
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

data class UploadedDoc(val name: String, val size: String, var isUploaded: Boolean = true)

@Composable
fun ApplicationFormScreen(
    opportunity: OpportunityEntity,
    currentUser: UserEntity?,
    onBackClick: () -> Unit,
    onSubmitSuccess: (ApplicationEntity) -> Unit
) {
    var step by remember { mutableIntStateOf(1) } // 1: Personal, 2: Education, 3: Documents, 4: Review

    // Pre-populate with saved profile data (Requirement 61: Profile Auto-Fill)
    var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var fatherName by remember { mutableStateOf(currentUser?.fatherName ?: "") }
    var cnic by remember { mutableStateOf(currentUser?.cnic ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var dob by remember { mutableStateOf(currentUser?.dob ?: "2000-01-01") }
    var gender by remember { mutableStateOf(currentUser?.gender ?: "Male") }
    var province by remember { mutableStateOf(currentUser?.province ?: "Punjab") }
    var district by remember { mutableStateOf(currentUser?.district ?: "Lahore") }
    var address by remember { mutableStateOf(currentUser?.address ?: "") }

    // Education
    var qualification by remember { mutableStateOf(currentUser?.educationLevel ?: "Bachelor") }
    var degreeTitle by remember { mutableStateOf("BS Computer Science") }
    var institute by remember { mutableStateOf("University of the Punjab") }
    var passingYear by remember { mutableStateOf("2024") }
    var marks by remember { mutableStateOf("3.65 CGPA / 82%") }
    var experienceDetails by remember { mutableStateOf(currentUser?.experience ?: "") }

    // Documents
    val documents = remember {
        mutableStateListOf(
            UploadedDoc("Passport Size Photograph (White BG)", "142 KB"),
            UploadedDoc("Computerized National Identity Card (Front & Back)", "420 KB"),
            UploadedDoc("Matriculation / SSC Transcript", "310 KB"),
            UploadedDoc("Intermediate / HSSC Transcript", "295 KB"),
            UploadedDoc("Bachelor Degree / Hope Certificate", "512 KB"),
            UploadedDoc("Domicile Certificate", "180 KB")
        )
    }

    var validationError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("application_form_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Apply: ${opportunity.organization}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = opportunity.title,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Step Indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepDot(stepNumber = 1, currentStep = step, label = "Personal")
                    HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = if (step >= 2) BrandPrimary else BrandSurfaceVariant)
                    StepDot(stepNumber = 2, currentStep = step, label = "Education")
                    HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = if (step >= 3) BrandPrimary else BrandSurfaceVariant)
                    StepDot(stepNumber = 3, currentStep = step, label = "Documents")
                    HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = if (step >= 4) BrandPrimary else BrandSurfaceVariant)
                    StepDot(stepNumber = 4, currentStep = step, label = "Review")
                }
            }
        }

        // Form Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (validationError.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = StatusError)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = validationError,
                            style = MaterialTheme.typography.bodySmall.copy(color = StatusError, fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            when (step) {
                1 -> {
                    // STEP 1: PERSONAL INFORMATION
                    Text(
                        text = "1. Personal Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = "Information auto-filled from your profile. Review and update as necessary.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Applicant Full Name *") },
                        modifier = Modifier.fillMaxWidth().testTag("app_input_name"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = fatherName,
                        onValueChange = { fatherName = it },
                        label = { Text("Father / Guardian Name *") },
                        modifier = Modifier.fillMaxWidth().testTag("app_input_father"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = cnic,
                        onValueChange = { cnic = it },
                        label = { Text("CNIC / B-Form Number * (e.g. 37405-1234567-1)") },
                        modifier = Modifier.fillMaxWidth().testTag("app_input_cnic"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Mobile *") },
                            modifier = Modifier.weight(1f).testTag("app_input_phone"),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Gender *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address *") },
                        modifier = Modifier.fillMaxWidth().testTag("app_input_email"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            label = { Text("Domicile Province *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("District *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Postal / Residential Address *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )
                }

                2 -> {
                    // STEP 2: EDUCATION & ACADEMICS
                    Text(
                        text = "2. Academic Qualifications",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = "Provide your highest degrees and examination scores.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = qualification,
                        onValueChange = { qualification = it },
                        label = { Text("Highest Qualification (e.g. Matric, Inter, BS, MS) *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = degreeTitle,
                        onValueChange = { degreeTitle = it },
                        label = { Text("Degree Title / Major Discipline *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = institute,
                        onValueChange = { institute = it },
                        label = { Text("Board / University / Institute *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = marks,
                            onValueChange = { marks = it },
                            label = { Text("Marks / CGPA *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = passingYear,
                            onValueChange = { passingYear = it },
                            label = { Text("Passing Year *") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = experienceDetails,
                        onValueChange = { experienceDetails = it },
                        label = { Text("Work Experience / Skills / Publications (if any)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 3
                    )
                }

                3 -> {
                    // STEP 3: DOCUMENT UPLOAD
                    Text(
                        text = "3. Required Documents Upload",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = "Secure document repository. Supported formats: JPG, PNG, PDF (Max 5MB).",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    documents.forEachIndexed { index, doc ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(10.dp),
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
                                        .clip(CircleShape)
                                        .background(if (doc.isUploaded) StatusSuccessContainer else BrandSurfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (doc.isUploaded) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                                        contentDescription = null,
                                        tint = if (doc.isUploaded) StatusSuccess else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = doc.name,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = if (doc.isUploaded) "Uploaded (${doc.size})" else "Not attached",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (doc.isUploaded) StatusSuccess else TextMuted
                                        )
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        documents[index] = doc.copy(isUploaded = !doc.isUploaded)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (doc.isUploaded) "Replace" else "Upload",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
                                    )
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // STEP 4: PREVIEW & VERIFICATION
                    Text(
                        text = "4. Review Application Preview",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                    Text(
                        text = "Please verify that all entered information is genuine before final submission.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Opportunity Details",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandPrimary)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = opportunity.title, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = opportunity.organization, color = TextSecondary, fontSize = 12.sp)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BrandSurfaceVariant)

                            Text(
                                text = "Applicant Summary",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandPrimary)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            ReviewRow("Full Name", fullName)
                            ReviewRow("Father Name", fatherName)
                            ReviewRow("CNIC", cnic)
                            ReviewRow("Mobile", phone)
                            ReviewRow("Email", email)
                            ReviewRow("Domicile", "$district, $province")
                            ReviewRow("Qualification", "$degreeTitle ($marks)")
                            ReviewRow("Institute", institute)
                            ReviewRow("Passing Year", passingYear)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BrandSurfaceVariant)

                            Text(
                                text = "Attached Documents",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandPrimary)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            documents.filter { it.isUploaded }.forEach { doc ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = doc.name, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = TextSecondary))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Undertaking Declaration
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = BrandPrimaryContainer.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "I solemnly declare that all statements made in this application are true, complete, and correct to the best of my knowledge and belief.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = TextPrimary)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom Controls
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = {
                            validationError = ""
                            step -= 1
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        validationError = ""
                        when (step) {
                            1 -> {
                                if (fullName.isBlank()) validationError = "Please enter your Full Name."
                                else if (cnic.isBlank()) validationError = "Please enter your CNIC Number."
                                else if (phone.isBlank()) validationError = "Please enter your Contact Mobile Number."
                                else step = 2
                            }
                            2 -> {
                                if (qualification.isBlank() || degreeTitle.isBlank()) {
                                    validationError = "Please specify your degree title."
                                } else {
                                    step = 3
                                }
                            }
                            3 -> {
                                val uploadedCount = documents.count { it.isUploaded }
                                if (uploadedCount < 2) {
                                    validationError = "Please attach at least your Photograph and CNIC copy."
                                } else {
                                    step = 4
                                }
                            }
                            4 -> {
                                // Final Submission
                                val application = ApplicationEntity(
                                    applicationNumber = "",
                                    userId = currentUser?.id ?: 1L,
                                    opportunityId = opportunity.id,
                                    opportunityTitle = opportunity.title,
                                    organization = opportunity.organization,
                                    category = opportunity.category,
                                    applicantFullName = fullName,
                                    applicantFatherName = fatherName,
                                    applicantCnic = cnic,
                                    applicantPhone = phone,
                                    applicantEmail = email,
                                    applicantDob = dob,
                                    applicantGender = gender,
                                    applicantProvince = province,
                                    applicantDistrict = district,
                                    applicantAddress = address,
                                    qualification = qualification,
                                    degree = degreeTitle,
                                    institute = institute,
                                    marks = marks,
                                    passingYear = passingYear,
                                    experienceDetails = experienceDetails,
                                    attachedDocumentsList = documents.filter { it.isUploaded }.joinToString(", ") { it.name },
                                    status = "SUBMITTED",
                                    statusNote = "Application submitted successfully and queued for official review."
                                )
                                onSubmitSuccess(application)
                            }
                        }
                    },
                    modifier = Modifier.testTag("app_form_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (step == 4) "Submit Application" else "Continue",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StepDot(stepNumber: Int, currentStep: Int, label: String) {
    val isCompleted = currentStep > stepNumber
    val isCurrent = currentStep == stepNumber
    val bg = if (isCompleted) StatusSuccess else if (isCurrent) BrandPrimary else BrandSurfaceVariant
    val textColor = if (isCurrent || isCompleted) BrandPrimary else TextMuted

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            } else {
                Text(
                    text = "$stepNumber",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrent) Color.White else TextSecondary
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
        )
    }
}

@Composable
fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.5.sp))
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 11.5.sp))
    }
}
