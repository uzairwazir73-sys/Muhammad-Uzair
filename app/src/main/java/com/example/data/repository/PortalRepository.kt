package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.ActivityLogEntity
import com.example.data.local.entity.AnnouncementEntity
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.FaqEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.SavedOpportunityEntity
import com.example.data.local.entity.ServiceRequestEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import kotlin.random.Random

class PortalRepository(private val db: AppDatabase) {

    // Current Logged-in User Session State
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Admin Session State
    private val _adminUser = MutableStateFlow<UserEntity?>(null)
    val adminUser: StateFlow<UserEntity?> = _adminUser.asStateFlow()

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Auth: Email Login
    suspend fun loginWithEmail(email: String, password: String):Result<UserEntity> {
        val user = db.userDao().getUserByEmail(email.trim().lowercase())
            ?: return Result.failure(Exception("No account found with this email"))
        
        if (user.isBlocked) {
            return Result.failure(Exception("This account has been suspended by administration"))
        }

        val hashed = hashPassword(password)
        if (user.passwordHash.isNotEmpty() && user.passwordHash != hashed) {
            return Result.failure(Exception("Invalid password"))
        }

        _currentUser.value = user
        return Result.success(user)
    }

    // Auth: Email Registration
    suspend fun registerWithEmail(
        fullName: String,
        email: String,
        password: String,
        phone: String = "",
        cnic: String = ""
    ): Result<UserEntity> {
        val normalizedEmail = email.trim().lowercase()
        val existing = db.userDao().getUserByEmail(normalizedEmail)
        if (existing != null) {
            return Result.failure(Exception("Account already exists with this email"))
        }

        val newUser = UserEntity(
            email = normalizedEmail,
            passwordHash = hashPassword(password),
            fullName = fullName.trim(),
            phone = phone.trim(),
            cnic = cnic.trim(),
            authProvider = "EMAIL",
            role = "USER"
        )
        val id = db.userDao().insertUser(newUser)
        val created = newUser.copy(id = id)
        _currentUser.value = created
        
        // Add welcome notification
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = id,
                title = "Welcome to UZair Gfx!",
                message = "Your portal account has been created successfully. Explore verified jobs, admissions, and scholarships now.",
                category = "GENERAL"
            )
        )
        return Result.success(created)
    }

    // Auth: Google Login
    suspend fun loginWithGoogle(
        googleEmail: String,
        googleName: String,
        googleId: String,
        profilePic: String = ""
    ): UserEntity {
        val normalizedEmail = googleEmail.trim().lowercase()
        val existing = db.userDao().getUserByEmail(normalizedEmail)
        val userToLogin = if (existing != null) {
            val updated = existing.copy(
                lastActiveAt = System.currentTimeMillis(),
                fullName = if (existing.fullName.isBlank()) googleName else existing.fullName,
                profilePicUri = if (existing.profilePicUri.isBlank()) profilePic else existing.profilePicUri
            )
            db.userDao().updateUser(updated)
            updated
        } else {
            // Check if superadmin email matches
            val role = if (normalizedEmail == "uzaircomputer73@gmail.com") "SUPER_ADMIN" else "USER"
            val newUser = UserEntity(
                email = normalizedEmail,
                fullName = googleName,
                profilePicUri = profilePic,
                authProvider = "GOOGLE",
                googleId = googleId,
                role = role
            )
            val newId = db.userDao().insertUser(newUser)
            val user = newUser.copy(id = newId)

            db.notificationDao().insertNotification(
                NotificationEntity(
                    userId = newId,
                    title = "Welcome to UZair Gfx!",
                    message = "Signed in with Google. Complete your profile to auto-fill future job & admission applications.",
                    category = "GENERAL"
                )
            )
            user
        }

        _currentUser.value = userToLogin
        return userToLogin
    }

    // Admin Login Verification
    suspend fun loginAdmin(email: String, password: String): Result<UserEntity> {
        val user = db.userDao().getUserByEmail(email.trim().lowercase())
            ?: return Result.failure(Exception("Admin account not found"))

        if (user.role == "USER") {
            return Result.failure(Exception("Unauthorized: Account does not possess Administrative privileges"))
        }

        if (user.isBlocked) {
            return Result.failure(Exception("Admin access revoked"))
        }

        val hashed = hashPassword(password)
        if (user.passwordHash.isNotEmpty() && user.passwordHash != hashed) {
            return Result.failure(Exception("Invalid admin credentials"))
        }

        _adminUser.value = user
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = user.fullName,
                adminEmail = user.email,
                action = "Admin Logged In",
                targetRecord = "Portal Access Granted (${user.role})"
            )
        )
        return Result.success(user)
    }

    suspend fun quickLoginAdminAsSuperAdmin(): UserEntity {
        var admin = db.userDao().getUserByEmail("uzaircomputer73@gmail.com")
        if (admin == null) {
            val newAdmin = UserEntity(
                email = "uzaircomputer73@gmail.com",
                fullName = "Uzair Computer (Admin)",
                role = "SUPER_ADMIN",
                authProvider = "GOOGLE"
            )
            val id = db.userDao().insertUser(newAdmin)
            admin = newAdmin.copy(id = id)
        }
        _adminUser.value = admin
        return admin
    }

    fun logout() {
        _currentUser.value = null
    }

    fun logoutAdmin() {
        _adminUser.value = null
    }

    suspend fun updateProfile(user: UserEntity) {
        db.userDao().updateUser(user)
        if (_currentUser.value?.id == user.id) {
            _currentUser.value = user
        }
    }

    // Opportunities
    fun getPublishedOpportunities(): Flow<List<OpportunityEntity>> = db.opportunityDao().getPublishedOpportunities()
    fun getAllOpportunities(): Flow<List<OpportunityEntity>> = db.opportunityDao().getAllOpportunities()
    fun getOpportunityById(id: Long): Flow<OpportunityEntity?> = db.opportunityDao().getOpportunityById(id)
    suspend fun getOpportunityByIdOnce(id: Long): OpportunityEntity? = db.opportunityDao().getOpportunityByIdOnce(id)
    fun getOpportunitiesByCategory(cat: String): Flow<List<OpportunityEntity>> = db.opportunityDao().getOpportunitiesByCategory(cat)
    fun getFeaturedOpportunities(): Flow<List<OpportunityEntity>> = db.opportunityDao().getFeaturedOpportunities()
    fun searchOpportunities(q: String): Flow<List<OpportunityEntity>> = db.opportunityDao().searchOpportunities(q)

    // Admin Opportunity Operations
    suspend fun insertOpportunity(opp: OpportunityEntity, adminUser: UserEntity? = null): Long {
        val id = db.opportunityDao().insertOpportunity(opp)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Created Opportunity",
                targetRecord = "${opp.category}: ${opp.title}"
            )
        )
        // Broadcast notification for new listing
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = 0,
                title = "New ${opp.category.replace('_', ' ')} Opportunity",
                message = "${opp.organization} has announced '${opp.title}'. Check eligibility and apply now.",
                category = opp.category
            )
        )
        return id
    }

    suspend fun updateOpportunity(opp: OpportunityEntity, adminUser: UserEntity? = null) {
        db.opportunityDao().updateOpportunity(opp)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Updated Opportunity",
                targetRecord = opp.title
            )
        )
    }

    suspend fun deleteOpportunity(id: Long, title: String, adminUser: UserEntity? = null) {
        db.opportunityDao().deleteOpportunityById(id)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Deleted Opportunity",
                targetRecord = title
            )
        )
    }

    suspend fun togglePublishStatus(id: Long, currentStatus: String, title: String, adminUser: UserEntity? = null) {
        val newStatus = if (currentStatus == "PUBLISHED") "DRAFT" else "PUBLISHED"
        db.opportunityDao().updateOpportunityStatus(id, newStatus)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Changed Status to $newStatus",
                targetRecord = title
            )
        )
    }

    // Applications
    fun getApplicationsForUser(userId: Long): Flow<List<ApplicationEntity>> = db.applicationDao().getApplicationsByUserId(userId)
    fun getAllApplications(): Flow<List<ApplicationEntity>> = db.applicationDao().getAllApplications()
    fun getApplicationById(id: Long): Flow<ApplicationEntity?> = db.applicationDao().getApplicationById(id)
    suspend fun getApplicationByNumber(num: String): ApplicationEntity? = db.applicationDao().getApplicationByNumber(num)

    suspend fun submitApplication(application: ApplicationEntity): String {
        val randomNum = Random.nextInt(100000, 999999)
        val appNumber = "UGX-$randomNum"
        val completeApp = application.copy(applicationNumber = appNumber)
        val id = db.applicationDao().insertApplication(completeApp)
        db.opportunityDao().incrementApplicationCount(application.opportunityId)

        // Add confirmation notification
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = application.userId,
                title = "Application Submitted ($appNumber)",
                message = "Your application for '${application.opportunityTitle}' has been received. Track progress using your Application ID.",
                category = "APPLICATION_UPDATE"
            )
        )

        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = application.applicantFullName,
                adminEmail = application.applicantEmail,
                action = "Submitted Application",
                targetRecord = "$appNumber for ${application.opportunityTitle}"
            )
        )
        return appNumber
    }

    suspend fun updateApplicationStatus(
        id: Long,
        appNumber: String,
        newStatus: String,
        statusNote: String,
        internalNote: String,
        applicantUserId: Long,
        oppTitle: String,
        adminUser: UserEntity? = null
    ) {
        db.applicationDao().updateStatus(
            id = id,
            status = newStatus,
            note = statusNote,
            internalNote = internalNote,
            updatedAt = System.currentTimeMillis()
        )

        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = applicantUserId,
                title = "Application Status Updated: $newStatus",
                message = "Your application $appNumber ($oppTitle) is now $newStatus. Note: $statusNote",
                category = "APPLICATION_UPDATE"
            )
        )

        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Updated Application Status to $newStatus",
                targetRecord = appNumber
            )
        )
    }

    // Bookmarks
    fun getSavedOpportunityIds(userId: Long): Flow<List<Long>> = db.savedOpportunityDao().getSavedOpportunityIds(userId)
    suspend fun toggleSavedOpportunity(userId: Long, opportunityId: Long) {
        val isSaved = db.savedOpportunityDao().isOpportunitySaved(userId, opportunityId)
        if (isSaved) {
            db.savedOpportunityDao().removeSavedOpportunity(userId, opportunityId)
        } else {
            db.savedOpportunityDao().saveOpportunity(
                SavedOpportunityEntity(userId = userId, opportunityId = opportunityId)
            )
        }
    }

    // Notifications
    fun getNotificationsForUser(userId: Long): Flow<List<NotificationEntity>> = db.notificationDao().getNotificationsForUser(userId)
    fun getUnreadNotificationCount(userId: Long): Flow<Int> = db.notificationDao().getUnreadCount(userId)
    suspend fun markNotificationAsRead(id: Long) = db.notificationDao().markAsRead(id)
    suspend fun markAllNotificationsAsRead(userId: Long) = db.notificationDao().markAllAsRead(userId)

    suspend fun broadcastNotification(title: String, message: String, category: String, adminUser: UserEntity? = null) {
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = 0,
                title = title,
                message = message,
                category = category
            )
        )
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Sent Broadcast Notification",
                targetRecord = title
            )
        )
    }

    // Announcements
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>> = db.announcementDao().getAllAnnouncements()
    suspend fun insertAnnouncement(ann: AnnouncementEntity, adminUser: UserEntity? = null) {
        db.announcementDao().insertAnnouncement(ann)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Created Announcement",
                targetRecord = ann.title
            )
        )
    }
    suspend fun deleteAnnouncement(id: Long) = db.announcementDao().deleteById(id)

    // FAQs
    fun getAllFaqs(): Flow<List<FaqEntity>> = db.faqDao().getAllFaqs()
    suspend fun insertFaq(faq: FaqEntity) = db.faqDao().insertFaq(faq)
    suspend fun deleteFaq(faq: FaqEntity) = db.faqDao().deleteFaq(faq)

    // Service Requests
    fun getAllServiceRequests(): Flow<List<ServiceRequestEntity>> = db.serviceRequestDao().getAllRequests()
    fun getServiceRequestsForUser(userId: Long): Flow<List<ServiceRequestEntity>> = db.serviceRequestDao().getRequestsByUserId(userId)
    suspend fun submitServiceRequest(req: ServiceRequestEntity): String {
        val reqNumber = "SRV-${Random.nextInt(10000, 99999)}"
        val complete = req.copy(requestId = reqNumber)
        db.serviceRequestDao().insertRequest(complete)
        return reqNumber
    }
    suspend fun updateServiceRequestStatus(id: Long, status: String) = db.serviceRequestDao().updateStatus(id, status)

    // Users Management
    fun getAllUsers(): Flow<List<UserEntity>> = db.userDao().getAllUsers()
    suspend fun setUserBlocked(userId: Long, blocked: Boolean, adminUser: UserEntity? = null) {
        db.userDao().setUserBlocked(userId, blocked)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = if (blocked) "Blocked User" else "Unblocked User",
                targetRecord = "User ID #$userId"
            )
        )
    }

    suspend fun updateUserRole(userId: Long, newRole: String, adminUser: UserEntity? = null) {
        db.userDao().updateUserRole(userId, newRole)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                adminName = adminUser?.fullName ?: "Admin",
                adminEmail = adminUser?.email ?: "admin@uzairgfx.portal",
                action = "Updated User Role to $newRole",
                targetRecord = "User ID #$userId"
            )
        )
    }

    // Admin Activity Logs
    fun getRecentActivityLogs(): Flow<List<ActivityLogEntity>> = db.activityLogDao().getRecentLogs()

    // Counts for Admin Dashboard
    fun getTotalUsersCount(): Flow<Int> = db.userDao().getUserCount()
    fun getTotalApplicationsCount(): Flow<Int> = db.applicationDao().getTotalApplicationsCount()
    fun getPendingApplicationsCount(): Flow<Int> = db.applicationDao().getPendingApplicationsCount()
    fun getApprovedApplicationsCount(): Flow<Int> = db.applicationDao().getApprovedApplicationsCount()
    fun getTotalOpportunityCount(): Flow<Int> = db.opportunityDao().getTotalOpportunityCount()
    fun getActiveOpportunityCount(): Flow<Int> = db.opportunityDao().getActiveOpportunityCount()

    // Export Helper
    fun generateApplicationsCsv(applications: List<ApplicationEntity>): String {
        val sb = StringBuilder()
        sb.append("ApplicationID,ApplicantName,CNIC,Phone,Email,Opportunity,Category,Organization,Status,SubmissionDate\n")
        applications.forEach { app ->
            sb.append("\"${app.applicationNumber}\",")
            sb.append("\"${app.applicantFullName}\",")
            sb.append("\"${app.applicantCnic}\",")
            sb.append("\"${app.applicantPhone}\",")
            sb.append("\"${app.applicantEmail}\",")
            sb.append("\"${app.opportunityTitle.replace("\"", "\"\"")}\",")
            sb.append("\"${app.category}\",")
            sb.append("\"${app.organization.replace("\"", "\"\"")}\",")
            sb.append("\"${app.status}\",")
            sb.append("\"${app.submissionDate}\"\n")
        }
        return sb.toString()
    }
}
