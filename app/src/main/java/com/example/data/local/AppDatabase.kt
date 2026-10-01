package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.ActivityLogDao
import com.example.data.local.dao.AnnouncementDao
import com.example.data.local.dao.ApplicationDao
import com.example.data.local.dao.FaqDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.OpportunityDao
import com.example.data.local.dao.SavedOpportunityDao
import com.example.data.local.dao.ServiceRequestDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.ActivityLogEntity
import com.example.data.local.entity.AnnouncementEntity
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.FaqEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.SavedOpportunityEntity
import com.example.data.local.entity.ServiceRequestEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        OpportunityEntity::class,
        ApplicationEntity::class,
        SavedOpportunityEntity::class,
        NotificationEntity::class,
        AnnouncementEntity::class,
        ActivityLogEntity::class,
        FaqEntity::class,
        ServiceRequestEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun opportunityDao(): OpportunityDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun savedOpportunityDao(): SavedOpportunityDao
    abstract fun notificationDao(): NotificationDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun activityLogDao(): ActivityLogDao
    abstract fun faqDao(): FaqDao
    abstract fun serviceRequestDao(): ServiceRequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uzair_gfx_database"
                )
                .addCallback(DatabaseSeederCallback())
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseSeederCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(database)
                }
            }
        }

        private suspend fun seedDatabase(db: AppDatabase) {
            val user = UserEntity(
                email = "uzaircomputer73@gmail.com",
                fullName = "Uzair Computer",
                fatherName = "Muhammad Sharif",
                cnic = "37405-1234567-1",
                phone = "0300-1234567",
                dob = "1998-05-14",
                gender = "Male",
                province = "Punjab",
                district = "Rawalpindi",
                address = "Main Market, Commercial Center, Rawalpindi",
                educationLevel = "Bachelor",
                skills = "Graphic Design, Online Form Filing, IT Support",
                experience = "5 Years in Online Application Services",
                authProvider = "GOOGLE",
                googleId = "uzair-google-admin-1",
                role = "SUPER_ADMIN"
            )
            db.userDao().insertUser(user)

            val now = System.currentTimeMillis()
            val dayMs = 24 * 3600 * 1000L

            val opportunities = listOf(
                // 1. Govt Job: FPSC
                OpportunityEntity(
                    category = "GOV_JOB",
                    title = "Assistant Director (BPS-17) - Case No. F.4-180/2026-R",
                    organization = "Federal Public Service Commission (FPSC)",
                    department = "Intelligence & Investigation / Inland Revenue",
                    location = "Islamabad / Anywhere in Pakistan",
                    province = "Federal",
                    city = "Islamabad",
                    bpsGrade = "BPS-17",
                    vacancies = 42,
                    genderRequirement = "Male / Female",
                    ageLimit = "22 - 30 Years (+5 Years General Relaxation)",
                    qualificationRequired = "Second Class or Grade 'C' Master's Degree / 16 Years BS in CS, IT, Law, Economics, or Business Administration",
                    experienceRequired = "Fresh Graduates Eligible",
                    salary = "BPS-17 Scale (~85,000 - 110,000 PKR)",
                    fee = "PKR 300/- (National Bank of Pakistan Challan)",
                    openingDate = "01-10-2026",
                    lastDate = "20-10-2026",
                    deadlineTimestamp = now + (19 * dayMs),
                    description = "FPSC invites online applications from Pakistani citizens for recruitment to temporary posts of Assistant Director in Federal Government departments.",
                    eligibilityCriteria = "Must possess 16 years of education from HEC recognized university. Domicile quota: Punjab=21, Sindh=9, KPK=5, Balochistan=3, Ex-FATA=2, AJK=2.",
                    requiredDocuments = "Paid Challan Form (32-A), CNIC, Degree Transcripts, Domicile Certificate, Passport Size Photo",
                    officialWebsite = "https://www.fpsc.gov.pk",
                    officialApplicationUrl = "https://online.fpsc.gov.pk",
                    sourceName = "FPSC Consolidated Advertisement No. 10/2026",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED",
                    isFeatured = true
                ),

                // 2. Govt Job: PPSC
                OpportunityEntity(
                    category = "GOV_JOB",
                    title = "Lecturer Computer Science (Male/Female) (BPS-17)",
                    organization = "Punjab Public Service Commission (PPSC)",
                    department = "Punjab Higher Education Department",
                    location = "Punjab (All Colleges)",
                    province = "Punjab",
                    city = "Lahore",
                    bpsGrade = "BPS-17",
                    vacancies = 115,
                    genderRequirement = "Male / Female / Transgender",
                    ageLimit = "21 - 28 Years (+7 Years Relaxation for Females, +5 Years for Males)",
                    qualificationRequired = "Master's Degree / BS (4 Years) in Computer Science / IT / SE (at least 2nd Division)",
                    experienceRequired = "No previous experience required",
                    salary = "BPS-17 (~75,000 - 95,000 PKR)",
                    fee = "PKR 600/- via PSID / JazzCash / EasyPaisa",
                    openingDate = "28-09-2026",
                    lastDate = "15-10-2026",
                    deadlineTimestamp = now + (14 * dayMs),
                    description = "PPSC announces 115 posts of College Lecturers in Computer Science for government degree colleges across Punjab.",
                    eligibilityCriteria = "Punjab Domicile is mandatory. Written test syllabus: 80% CS Subject questions, 20% General Knowledge.",
                    requiredDocuments = "PSID Fee receipt, CNIC, Matric/Inter/BS Degrees, Punjab Domicile, Photo",
                    officialWebsite = "https://www.ppsc.gop.pk",
                    officialApplicationUrl = "https://www.ppsc.gop.pk/(S(k5t1))/Jobs.aspx",
                    sourceName = "PPSC Advertisement No. 24/2026",
                    isVerifiedSource = true,
                    verificationDate = "28-09-2026",
                    status = "PUBLISHED",
                    isFeatured = true
                ),

                // 3. University: NUST
                OpportunityEntity(
                    category = "UNIVERSITY",
                    title = "Undergraduate Fall 2026 Admissions (NET Series)",
                    organization = "National University of Sciences & Technology (NUST)",
                    department = "SEECS / SMME / NBS / S3H",
                    location = "Sector H-12, Islamabad",
                    province = "Federal",
                    city = "Islamabad",
                    degreeLevel = "Undergraduate (BS / BE / BBA)",
                    vacancies = 2400,
                    genderRequirement = "Male / Female",
                    ageLimit = "Open",
                    qualificationRequired = "F.Sc (Pre-Engineering / ICS / Pre-Medical with Add. Math) or A-Levels with min 60% marks",
                    experienceRequired = "N/A",
                    salary = "N/A",
                    fee = "Admission Fee: PKR 35,000 | NET Registration: PKR 5,000",
                    openingDate = "01-10-2026",
                    lastDate = "25-10-2026",
                    deadlineTimestamp = now + (24 * dayMs),
                    fundingType = "Partial / HEC Need Based Available",
                    description = "NUST Islamabad opens undergraduate admissions for Bachelor of Computer Science, Software Engineering, AI, Electrical Engineering, and Business.",
                    eligibilityCriteria = "Minimum 60% aggregate in SSC and HSSC Part-1. Admission based on NUST Entry Test (NET) score.",
                    requiredDocuments = "SSC Certificate/DMC, HSSC-1 Hope Certificate / DMC, CNIC/B-Form, Photo",
                    officialWebsite = "https://nust.edu.pk",
                    officialApplicationUrl = "https://ugadmissions.nust.edu.pk",
                    sourceName = "NUST Undergraduate Directorate",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED",
                    isFeatured = true
                ),

                // 4. University: UET Lahore
                OpportunityEntity(
                    category = "UNIVERSITY",
                    title = "B.Sc Engineering & Technology Admissions 2026",
                    organization = "University of Engineering and Technology (UET) Lahore",
                    department = "Faculty of Electrical, Computer & Mechanical Engineering",
                    location = "G.T Road, Lahore",
                    province = "Punjab",
                    city = "Lahore",
                    degreeLevel = "Undergraduate",
                    vacancies = 1800,
                    genderRequirement = "Male / Female",
                    qualificationRequired = "F.Sc Pre-Engineering / ICS with min 60% + ECAT Test",
                    fee = "Application Fee PKR 2,500",
                    openingDate = "05-10-2026",
                    lastDate = "22-10-2026",
                    deadlineTimestamp = now + (21 * dayMs),
                    description = "UET Lahore announces merit-based engineering and computing degree admissions across Main and sub-campuses.",
                    eligibilityCriteria = "ECAT 2026 valid score required. 30% ECAT + 70% Intermediate merit formula.",
                    requiredDocuments = "ECAT Result Card, HSSC DMC, SSC DMC, Domicile, CNIC/B-Form",
                    officialWebsite = "https://uet.edu.pk",
                    officialApplicationUrl = "https://admission.uet.edu.pk",
                    sourceName = "UET Lahore Admission Cell",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED"
                ),

                // 5. College: GCU Lahore
                OpportunityEntity(
                    category = "COLLEGE",
                    title = "Intermediate Admissions 2026 (Pre-Med, Pre-Eng, ICS)",
                    organization = "Government College University (GCU) Lahore",
                    department = "Intermediate Studies Board",
                    location = "Katchery Road, Lahore",
                    province = "Punjab",
                    city = "Lahore",
                    degreeLevel = "Intermediate (FA / FSc / ICS / I.Com)",
                    vacancies = 1200,
                    genderRequirement = "Male / Co-Education for specific disciplines",
                    qualificationRequired = "Matriculation (BISE or O-Levels) with min 70% marks for Science & ICS",
                    fee = "Prospectus & Registration Fee PKR 1,500",
                    openingDate = "01-10-2026",
                    lastDate = "18-10-2026",
                    deadlineTimestamp = now + (17 * dayMs),
                    description = "GCU Lahore invites applications for admission to 2-year Intermediate programs for academic session 2026-2028.",
                    eligibilityCriteria = "Passed SSC exam in first attempt. Merit based on Matric marks + interview where applicable.",
                    requiredDocuments = "Matric Result Card, B-Form / CNIC, Father CNIC, Character Certificate, Photographs",
                    officialWebsite = "https://gcu.edu.pk",
                    officialApplicationUrl = "https://gcuonline.pk/admissions",
                    sourceName = "GCU Lahore Admission Committee",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED"
                ),

                // 6. Scholarship: HEC Indigenous
                OpportunityEntity(
                    category = "SCHOLARSHIP",
                    title = "HEC Indigenous PhD Fellowship Program Phase-IV",
                    organization = "Higher Education Commission of Pakistan (HEC)",
                    department = "Human Resource Development Division",
                    location = "All Pakistani Universities",
                    province = "National",
                    city = "Islamabad",
                    degreeLevel = "PhD",
                    vacancies = 500,
                    genderRequirement = "Male / Female",
                    ageLimit = "Maximum 35 Years",
                    qualificationRequired = "MS / MPhil with minimum CGPA 3.0 out of 4.0 or 1st Division in annual system",
                    fee = "No application fee for online portal",
                    openingDate = "01-10-2026",
                    lastDate = "31-10-2026",
                    deadlineTimestamp = now + (30 * dayMs),
                    fundingType = "Fully Funded",
                    description = "HEC Indigenous Scholarship offers full tuition waiver, books allowance, laptop, and monthly stipend of PKR 100,000 for full-time PhD scholars.",
                    eligibilityCriteria = "Pakistani/AJK nationals with full-time PhD enrollment in HEC recognized institutions. GAT Subject / HAT test score required.",
                    requiredDocuments = "Attested Degrees & Transcripts, Research Proposal, PhD Admission Offer Letter, CNIC, Domicile",
                    officialWebsite = "https://hec.gov.pk",
                    officialApplicationUrl = "https://eportal.hec.gov.pk",
                    sourceName = "HEC Official Gazette Notification",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED",
                    isFeatured = true
                ),

                // 7. Scholarship: Scottish Scholarship
                OpportunityEntity(
                    category = "SCHOLARSHIP",
                    title = "Scottish Scholarship for Pakistani Women 2026",
                    organization = "British Council Pakistan & Scottish Government",
                    department = "Education Services",
                    location = "Pakistani Public / Private Universities",
                    province = "National",
                    city = "Islamabad",
                    degreeLevel = "Undergraduate & Masters",
                    vacancies = 250,
                    genderRequirement = "Female Only",
                    qualificationRequired = "Female students accepted into 4-year Bachelor's or 1-2 year Master's program in Education, Health, STEM or Food Security",
                    fee = "Free Application",
                    openingDate = "15-09-2026",
                    lastDate = "12-10-2026",
                    deadlineTimestamp = now + (11 * dayMs),
                    fundingType = "Fully Funded",
                    description = "Provides complete tuition fee for approved universities, hostel accommodation costs, and travel allowances for women from underprivileged backgrounds.",
                    eligibilityCriteria = "Must be Pakistani woman residing in Pakistan. Enrolled in public sector university or accredited private university.",
                    requiredDocuments = "University Admission Letter, Fee Voucher Copy, Income Certificate, CNIC / B-Form",
                    officialWebsite = "https://www.britishcouncil.pk",
                    officialApplicationUrl = "https://www.britishcouncil.pk/programmes/education/scholarships",
                    sourceName = "British Council Official Announcement",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED"
                ),

                // 8. Hajj Information: Ministry of Religious Affairs
                OpportunityEntity(
                    category = "HAJJ",
                    title = "Government Hajj Scheme 2026 Official Policy & Guidelines",
                    organization = "Ministry of Religious Affairs & Interfaith Harmony",
                    department = "Hajj Directorate Pakistan",
                    location = "Islamabad / Designated Bank Branches",
                    province = "National",
                    city = "Islamabad",
                    degreeLevel = "General Public",
                    vacancies = 89000,
                    genderRequirement = "Male / Female (Mahram / Female group as per policy)",
                    ageLimit = "Above 12 Years as per Saudi MoH Regulations",
                    fee = "Total Package approx. PKR 1,075,000 (North Zone) / 1,065,000 (South Zone)",
                    openingDate = "01-10-2026",
                    lastDate = "10-11-2026",
                    deadlineTimestamp = now + (40 * dayMs),
                    description = "Official guidelines, quota distribution, sponsorship scheme (remittance in foreign currency), and regular scheme application process through designated banks.",
                    eligibilityCriteria = "Machine readable passport valid up to Dec 2026. NADRA CNIC/NICOP. Medical fitness certificate. Those who performed Hajj in last 5 years ineligible for regular quota.",
                    requiredDocuments = "Valid Passport, CNIC, 4 Blue-background Photos, Medical Fitness Form, Bank Challan",
                    officialWebsite = "https://mora.gov.pk",
                    officialApplicationUrl = "https://hajjinfo.org",
                    sourceName = "Ministry of Religious Affairs Hajj Directorate Notification",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED",
                    isFeatured = true
                ),

                // 9. Umrah Information & Services
                OpportunityEntity(
                    category = "UMRAH",
                    title = "Authorized Umrah Travel Protocols & Nusuk Platform Guidelines",
                    organization = "Ministry of Hajj and Umrah / Verified Travel Consortia",
                    department = "Pilgrim Services Oversight",
                    location = "Makkah & Madinah, KSA",
                    province = "National",
                    city = "Islamabad",
                    degreeLevel = "General Public",
                    fee = "Visa Processing & Biometrics from PKR 35,000",
                    openingDate = "01-08-2026",
                    lastDate = "30-04-2027",
                    deadlineTimestamp = now + (200 * dayMs),
                    description = "Complete guide to obtaining direct tourist/Umrah e-visa, biometric submission via Saudi Visa Bio app, Nusuk slot reservation for Rawdah Mubarak, and authorized agency packages.",
                    eligibilityCriteria = "Valid passport with 6 months validity. Confirmed return flight and hotel booking in Makkah/Madinah.",
                    requiredDocuments = "Passport Scan, Passport Size Photo with White Background, Polio/Meningitis Vaccination Certificate",
                    officialWebsite = "https://www.nusuk.sa",
                    officialApplicationUrl = "https://visa.mofa.gov.sa",
                    sourceName = "Saudi Ministry of Tourism & Hajj Protocols",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED"
                ),

                // 10. Private Job: Systems Limited
                OpportunityEntity(
                    category = "PRIVATE_JOB",
                    title = "Senior Android / Mobile Engineer (Kotlin & Jetpack Compose)",
                    organization = "Systems Limited Pakistan",
                    department = "Digital Enterprise & Cloud Solutions",
                    location = "Lahore / Karachi / Islamabad (Hybrid)",
                    province = "Punjab",
                    city = "Lahore",
                    vacancies = 4,
                    genderRequirement = "Male / Female",
                    qualificationRequired = "BS in Computer Science, Software Engineering or equivalent",
                    experienceRequired = "3+ Years in Modern Android Development",
                    salary = "PKR 250,000 - 380,000 / month + OPD/IPD + Fuel Allowance",
                    openingDate = "01-10-2026",
                    lastDate = "24-10-2026",
                    deadlineTimestamp = now + (23 * dayMs),
                    description = "Systems Limited is hiring experienced Android Engineers to architect high-performance fintech and enterprise mobile applications using Kotlin, M3 Compose, Coroutines and Room.",
                    eligibilityCriteria = "Strong knowledge of MVVM/MVI, Jetpack Compose, Unit Testing, Room, REST APIs, Git.",
                    requiredDocuments = "Updated CV/Resume, Portfolio/GitHub Links, Degree Verification",
                    officialWebsite = "https://www.systemsltd.com",
                    officialApplicationUrl = "https://www.systemsltd.com/careers",
                    sourceName = "Systems Limited Careers Portal",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED"
                ),

                // 11. Online Services
                OpportunityEntity(
                    category = "ONLINE_SERVICE",
                    title = "Online Form Filing & Document Preparation Assistance",
                    organization = "UZair Gfx Facilitation Desk",
                    department = "Citizen Application Support Desk",
                    location = "Nationwide Online Service",
                    province = "Federal",
                    city = "Islamabad",
                    fee = "Nominal Assistance Fee PKR 500 - 1,000",
                    openingDate = "01-01-2026",
                    lastDate = "31-12-2026",
                    deadlineTimestamp = now + (90 * dayMs),
                    description = "Expert help filling FPSC, PPSC, NUST, HEC, or Passport/NADRA applications without errors. We format photos to exact pixel specs, compress PDFs, and verify Challan details.",
                    eligibilityCriteria = "Open to all students and job seekers across Pakistan.",
                    requiredDocuments = "Required opportunity documents (CNIC, Transcripts, Photos)",
                    officialWebsite = "https://uzairgfx.portal",
                    officialApplicationUrl = "https://uzairgfx.portal/apply",
                    sourceName = "UZair Gfx Verified Online Portal Support",
                    isVerifiedSource = true,
                    verificationDate = "01-10-2026",
                    status = "PUBLISHED"
                )
            )
            db.opportunityDao().insertOpportunities(opportunities)

            // Seed Announcements
            val announcements = listOf(
                AnnouncementEntity(
                    title = "FPSC Adv 10/2026 Last Date Reminder",
                    description = "Candidates applying for Assistant Director BPS-17 must submit fee and online application before October 20.",
                    category = "GOV_JOB",
                    badgeText = "Closing Soon",
                    isUrgent = true,
                    publishedAt = now - (2 * dayMs)
                ),
                AnnouncementEntity(
                    title = "Government Hajj Scheme 2026 Quota Announced",
                    description = "Ministry of Religious Affairs issues guidelines for regular and sponsorship Hajj schemes. Read details in Hajj section.",
                    category = "HAJJ",
                    badgeText = "Policy Update",
                    isUrgent = false,
                    publishedAt = now - (1 * dayMs)
                ),
                AnnouncementEntity(
                    title = "HEC PhD Indigenous Phase-IV Applications Open",
                    description = "500 Fully Funded scholarships available for research scholars in Pakistani universities.",
                    category = "SCHOLARSHIP",
                    badgeText = "Fully Funded",
                    isUrgent = false,
                    publishedAt = now - (3 * dayMs)
                )
            )
            for (ann in announcements) {
                db.announcementDao().insertAnnouncement(ann)
            }

            // Seed FAQs
            val faqs = listOf(
                FaqEntity(
                    question = "What is UZair Gfx Portal?",
                    answer = "UZair Gfx is a centralized Pakistani portal for discovering verified admissions, government & private jobs, scholarships, Hajj/Umrah guidelines, and assisted online application submission services.",
                    category = "General",
                    orderIndex = 1
                ),
                FaqEntity(
                    question = "How do I login with Google / Gmail?",
                    answer = "Click 'Continue with Google' on the login screen. Your Google account details will be securely used to create your UZair Gfx account and auto-fill compatible details.",
                    category = "Account",
                    orderIndex = 2
                ),
                FaqEntity(
                    question = "How does the 'Apply Now' workflow work?",
                    answer = "Click Apply Now on any opportunity. The system automatically pulls your saved profile data (CNIC, phone, education) into the form. Attach required documents, preview your data, and submit to receive a unique UGX-XXXXXX tracking receipt.",
                    category = "Applications",
                    orderIndex = 3
                ),
                FaqEntity(
                    question = "Are the opportunity links and gazettes verified?",
                    answer = "Yes! Every listing includes an official source badge, official gazette/advertisement reference, and direct links to the official department or university website.",
                    category = "Verification",
                    orderIndex = 4
                ),
                FaqEntity(
                    question = "How can I contact UZair Gfx support?",
                    answer = "You can reach us directly via the Help & Support tab, submit a service request, or contact our WhatsApp support desk at 0300-1234567.",
                    category = "Support",
                    orderIndex = 5
                )
            )
            db.faqDao().insertFaqs(faqs)

            // Seed Initial Activity Log
            db.activityLogDao().insertLog(
                ActivityLogEntity(
                    adminName = "System",
                    adminEmail = "system@uzairgfx.portal",
                    action = "System Initialized",
                    targetRecord = "Database seeded with verified Pakistani opportunities"
                )
            )
        }
    }
}
