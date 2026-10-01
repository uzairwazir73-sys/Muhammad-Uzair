package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.components.ALL_CATEGORIES
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminOpportunityEditorScreen(
    adminUser: UserEntity?,
    existingOpportunity: OpportunityEntity?,
    onSave: (OpportunityEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var category by remember { mutableStateOf(existingOpportunity?.category ?: "GOV_JOB") }
    var title by remember { mutableStateOf(existingOpportunity?.title ?: "") }
    var organization by remember { mutableStateOf(existingOpportunity?.organization ?: "") }
    var department by remember { mutableStateOf(existingOpportunity?.department ?: "") }
    var location by remember { mutableStateOf(existingOpportunity?.location ?: "Pakistan") }
    var province by remember { mutableStateOf(existingOpportunity?.province ?: "Federal") }
    var city by remember { mutableStateOf(existingOpportunity?.city ?: "Islamabad") }
    var bpsGrade by remember { mutableStateOf(existingOpportunity?.bpsGrade ?: "BPS-17") }
    var vacancies by remember { mutableIntStateOf(existingOpportunity?.vacancies ?: 1) }
    var genderRequirement by remember { mutableStateOf(existingOpportunity?.genderRequirement ?: "Male / Female") }
    var ageLimit by remember { mutableStateOf(existingOpportunity?.ageLimit ?: "18 - 30 Years") }
    var qualificationRequired by remember { mutableStateOf(existingOpportunity?.qualificationRequired ?: "Bachelor / Master") }
    var experienceRequired by remember { mutableStateOf(existingOpportunity?.experienceRequired ?: "Fresh") }
    var salary by remember { mutableStateOf(existingOpportunity?.salary ?: "BPS Scale") }
    var fee by remember { mutableStateOf(existingOpportunity?.fee ?: "PKR 500/-") }
    var openingDate by remember { mutableStateOf(existingOpportunity?.openingDate ?: "01-10-2026") }
    var lastDate by remember { mutableStateOf(existingOpportunity?.lastDate ?: "25-10-2026") }
    var fundingType by remember { mutableStateOf(existingOpportunity?.fundingType ?: "Fully Funded") }
    var description by remember { mutableStateOf(existingOpportunity?.description ?: "") }
    var eligibilityCriteria by remember { mutableStateOf(existingOpportunity?.eligibilityCriteria ?: "") }
    var requiredDocuments by remember { mutableStateOf(existingOpportunity?.requiredDocuments ?: "CNIC, Educational Certificates, Domicile, Photo") }
    var officialWebsite by remember { mutableStateOf(existingOpportunity?.officialWebsite ?: "https://") }
    var officialApplicationUrl by remember { mutableStateOf(existingOpportunity?.officialApplicationUrl ?: "https://") }
    var sourceName by remember { mutableStateOf(existingOpportunity?.sourceName ?: "Official Gazette / Press Notification") }
    var isVerifiedSource by remember { mutableStateOf(existingOpportunity?.isVerifiedSource ?: true) }
    var isFeatured by remember { mutableStateOf(existingOpportunity?.isFeatured ?: false) }
    var status by remember { mutableStateOf(existingOpportunity?.status ?: "PUBLISHED") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_opportunity_editor_screen")
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (existingOpportunity == null) "New Opportunity" else "Edit Opportunity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = {
                        val opp = OpportunityEntity(
                            id = existingOpportunity?.id ?: 0L,
                            category = category,
                            title = title,
                            organization = organization,
                            department = department,
                            location = location,
                            province = province,
                            city = city,
                            bpsGrade = bpsGrade,
                            vacancies = vacancies,
                            genderRequirement = genderRequirement,
                            ageLimit = ageLimit,
                            qualificationRequired = qualificationRequired,
                            experienceRequired = experienceRequired,
                            salary = salary,
                            fee = fee,
                            openingDate = openingDate,
                            lastDate = lastDate,
                            deadlineTimestamp = System.currentTimeMillis() + (14 * 24 * 3600 * 1000L),
                            fundingType = fundingType,
                            description = description,
                            eligibilityCriteria = eligibilityCriteria,
                            requiredDocuments = requiredDocuments,
                            officialWebsite = officialWebsite,
                            officialApplicationUrl = officialApplicationUrl,
                            sourceName = sourceName,
                            isVerifiedSource = isVerifiedSource,
                            verificationDate = "01-10-2026",
                            status = status,
                            isFeatured = isFeatured
                        )
                        onSave(opp)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_save_opp_btn")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save & Publish")
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
            // Category selector
            Text("Opportunity Category *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ALL_CATEGORIES.filter { it.id != "ALL" }.forEach { cat ->
                    val isSel = category == cat.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) BrandPrimary else BrandSurfaceVariant)
                            .clickable { category = cat.id }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSel) Color.White else TextPrimary,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Opportunity Title *") },
                modifier = Modifier.fillMaxWidth().testTag("editor_title_input"),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = organization,
                onValueChange = { organization = it },
                label = { Text("Organization / Institution / University *") },
                modifier = Modifier.fillMaxWidth().testTag("editor_org_input"),
                shape = RoundedCornerShape(10.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = bpsGrade,
                    onValueChange = { bpsGrade = it },
                    label = { Text("BPS / Grade / Degree Level") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = fee,
                    onValueChange = { fee = it },
                    label = { Text("Application Fee") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = lastDate,
                    onValueChange = { lastDate = it },
                    label = { Text("Application Last Date *") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = ageLimit,
                    onValueChange = { ageLimit = it },
                    label = { Text("Age Limit") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            OutlinedTextField(
                value = qualificationRequired,
                onValueChange = { qualificationRequired = it },
                label = { Text("Required Qualification *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opportunity Scope & Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                minLines = 3
            )

            OutlinedTextField(
                value = eligibilityCriteria,
                onValueChange = { eligibilityCriteria = it },
                label = { Text("Eligibility Criteria") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                minLines = 2
            )

            OutlinedTextField(
                value = requiredDocuments,
                onValueChange = { requiredDocuments = it },
                label = { Text("Required Documents Checklist (comma-separated)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            HorizontalDivider(color = BrandSurfaceVariant)

            Text("Source Verification (Official Reference)", fontWeight = FontWeight.Bold, fontSize = 13.sp)

            OutlinedTextField(
                value = sourceName,
                onValueChange = { sourceName = it },
                label = { Text("Official Source / Gazette Reference *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = officialWebsite,
                onValueChange = { officialWebsite = it },
                label = { Text("Official Organization Website URL") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isVerifiedSource, onCheckedChange = { isVerifiedSource = it })
                Spacer(modifier = Modifier.width(6.dp))
                Text("Mark as Verified Official Source", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                Spacer(modifier = Modifier.width(6.dp))
                Text("Feature this opportunity on Home Screen", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
