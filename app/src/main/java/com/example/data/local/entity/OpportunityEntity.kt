package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opportunities")
data class OpportunityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "UNIVERSITY", "COLLEGE", "GOV_JOB", "PRIVATE_JOB", "SCHOLARSHIP", "HAJJ", "UMRAH", "ONLINE_SERVICE"
    val title: String,
    val organization: String,
    val department: String = "",
    val location: String = "Pakistan",
    val province: String = "Federal",
    val city: String = "Islamabad",
    val degreeLevel: String = "", // e.g. "Undergraduate", "Masters", "PhD", "Intermediate"
    val bpsGrade: String = "",    // e.g. "BPS-17"
    val vacancies: Int = 1,
    val genderRequirement: String = "Male / Female",
    val ageLimit: String = "18 - 35 Years",
    val qualificationRequired: String = "Bachelor / Master",
    val experienceRequired: String = "Fresh",
    val salary: String = "Market Competitive",
    val fee: String = "Free / As per Gazette",
    val openingDate: String = "01-10-2026",
    val lastDate: String = "25-10-2026",
    val deadlineTimestamp: Long = System.currentTimeMillis() + 14 * 24 * 3600 * 1000L,
    val fundingType: String = "Fully Funded", // "Fully Funded", "Partial", "Self-Finance", "N/A"
    val description: String = "",
    val eligibilityCriteria: String = "",
    val requiredDocuments: String = "CNIC, Educational Certificates, Domicile, Passport Photo",
    val officialWebsite: String = "",
    val officialApplicationUrl: String = "",
    val sourceName: String = "Official Gazette",
    val isVerifiedSource: Boolean = true,
    val verificationDate: String = "01-10-2026",
    val status: String = "PUBLISHED", // "PUBLISHED", "DRAFT", "EXPIRED", "ARCHIVED"
    val isFeatured: Boolean = false,
    val totalApplicationsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
