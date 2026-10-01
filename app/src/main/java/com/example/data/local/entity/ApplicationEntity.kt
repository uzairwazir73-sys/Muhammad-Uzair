package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val applicationNumber: String, // e.g. "UGX-783921"
    val userId: Long,
    val opportunityId: Long,
    val opportunityTitle: String,
    val organization: String,
    val category: String,
    val applicantFullName: String,
    val applicantFatherName: String = "",
    val applicantCnic: String,
    val applicantPhone: String,
    val applicantEmail: String,
    val applicantDob: String = "",
    val applicantGender: String = "Male",
    val applicantProvince: String = "",
    val applicantDistrict: String = "",
    val applicantAddress: String = "",
    val qualification: String = "",
    val degree: String = "",
    val institute: String = "",
    val marks: String = "",
    val passingYear: String = "",
    val experienceDetails: String = "",
    val attachedDocumentsList: String = "CNIC Front, CNIC Back, Passport Photo, Degree",
    val status: String = "SUBMITTED", // "SUBMITTED", "UNDER_REVIEW", "CORRECTION_REQUIRED", "APPROVED", "REJECTED", "COMPLETED"
    val statusNote: String = "Application received and queued for review.",
    val internalAdminNote: String = "",
    val submissionDate: Long = System.currentTimeMillis(),
    val lastUpdatedDate: Long = System.currentTimeMillis()
)
