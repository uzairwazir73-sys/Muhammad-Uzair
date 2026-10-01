package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AnnouncementEntity
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.FaqEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.ServiceRequestEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.PortalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    // User Screens
    object Splash : Screen()
    object Auth : Screen()
    object Home : Screen()
    object Explore : Screen()
    data class OpportunityDetail(val id: Long) : Screen()
    data class ApplicationForm(val opportunityId: Long) : Screen()
    data class ApplicationReceipt(val appNumber: String) : Screen()
    object MyApplications : Screen()
    data class ApplicationDetail(val applicationId: Long) : Screen()
    object SavedOpportunities : Screen()
    object Notifications : Screen()
    object Profile : Screen()
    object OnlineServices : Screen()
    object FaqSupport : Screen()

    // Admin Screens
    object AdminLogin : Screen()
    object AdminDashboard : Screen()
    object AdminOpportunities : Screen()
    data class AdminOpportunityEditor(val opportunityId: Long = 0L) : Screen()
    object AdminApplications : Screen()
    data class AdminApplicationReview(val applicationId: Long) : Screen()
    object AdminUsers : Screen()
    object AdminContent : Screen()
    object AdminLogs : Screen()
    object AdminSettings : Screen()
}

data class FilterState(
    val category: String = "ALL", // "ALL", "UNIVERSITY", "COLLEGE", "GOV_JOB", "PRIVATE_JOB", "SCHOLARSHIP", "HAJJ", "UMRAH", "ONLINE_SERVICE"
    val province: String = "ALL",
    val degreeLevel: String = "ALL",
    val fundingType: String = "ALL",
    val gender: String = "ALL",
    val sortBy: String = "NEWEST" // "NEWEST", "DEADLINE", "A-Z"
)

class PortalViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = PortalRepository(db)

    // Navigation Stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Splash))
    val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Splash).also { flow ->
        viewModelScope.launch {
            _screenStack.collect { stack ->
                flow.value = stack.lastOrNull() ?: Screen.Home
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _screenStack.value = _screenStack.value + screen
    }

    fun navigateBack(): Boolean {
        if (_screenStack.value.size > 1) {
            _screenStack.value = _screenStack.value.dropLast(1)
            return true
        }
        return false
    }

    fun resetTo(screen: Screen) {
        _screenStack.value = listOf(screen)
    }

    // Auth States
    val currentUser = repository.currentUser
    val adminUser = repository.adminUser

    // Toast/Snack message
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun showToast(msg: String) {
        viewModelScope.launch { _toastEvent.emit(msg) }
    }

    // Search & Filtering
    val searchQuery = MutableStateFlow("")
    val filterState = MutableStateFlow(FilterState())

    // All Published Opportunities
    val publishedOpportunities: StateFlow<List<OpportunityEntity>> = repository.getPublishedOpportunities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Opportunities
    val filteredOpportunities: StateFlow<List<OpportunityEntity>> = combine(
        publishedOpportunities,
        searchQuery,
        filterState
    ) { opps, query, filter ->
        opps.filter { opp ->
            val matchesQuery = query.isBlank() ||
                opp.title.contains(query, ignoreCase = true) ||
                opp.organization.contains(query, ignoreCase = true) ||
                opp.qualificationRequired.contains(query, ignoreCase = true) ||
                opp.city.contains(query, ignoreCase = true) ||
                opp.province.contains(query, ignoreCase = true)

            val matchesCategory = filter.category == "ALL" || opp.category == filter.category
            val matchesProvince = filter.province == "ALL" || opp.province.equals(filter.province, ignoreCase = true) || opp.province == "National"
            val matchesFunding = filter.fundingType == "ALL" || opp.fundingType.contains(filter.fundingType, ignoreCase = true)
            val matchesGender = filter.gender == "ALL" || opp.genderRequirement.contains(filter.gender, ignoreCase = true)

            matchesQuery && matchesCategory && matchesProvince && matchesFunding && matchesGender
        }.let { list ->
            when (filter.sortBy) {
                "DEADLINE" -> list.sortedBy { it.deadlineTimestamp }
                "A-Z" -> list.sortedBy { it.title }
                else -> list.sortedByDescending { it.createdAt }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Featured & Announcements
    val featuredOpportunities: StateFlow<List<OpportunityEntity>> = repository.getFeaturedOpportunities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val announcements: StateFlow<List<AnnouncementEntity>> = repository.getAllAnnouncements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val faqs: StateFlow<List<FaqEntity>> = repository.getAllFaqs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User Saved IDs
    val savedOpportunityIds: StateFlow<List<Long>> = combine(
        currentUser,
        repository.getPublishedOpportunities()
    ) { user, _ ->
        user?.id ?: 0L
    }.let { userFlow ->
        MutableStateFlow<List<Long>>(emptyList()).also { stateFlow ->
            viewModelScope.launch {
                userFlow.collect { uid ->
                    if (uid > 0) {
                        repository.getSavedOpportunityIds(uid).collect { stateFlow.value = it }
                    } else {
                        stateFlow.value = emptyList()
                    }
                }
            }
        }
    }

    fun toggleSaveOpportunity(oppId: Long) {
        val user = currentUser.value
        if (user == null) {
            showToast("Please login or continue with Google to save opportunities")
            navigateTo(Screen.Auth)
            return
        }
        viewModelScope.launch {
            repository.toggleSavedOpportunity(user.id, oppId)
            val isNowSaved = !savedOpportunityIds.value.contains(oppId)
            showToast(if (isNowSaved) "Saved to your bookmarks" else "Removed from bookmarks")
        }
    }

    // User Applications
    val myApplications: StateFlow<List<ApplicationEntity>> = MutableStateFlow<List<ApplicationEntity>>(emptyList()).also { stateFlow ->
        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    repository.getApplicationsForUser(user.id).collect { stateFlow.value = it }
                } else {
                    stateFlow.value = emptyList()
                }
            }
        }
    }

    // Notifications
    val userNotifications: StateFlow<List<NotificationEntity>> = MutableStateFlow<List<NotificationEntity>>(emptyList()).also { stateFlow ->
        viewModelScope.launch {
            currentUser.collect { user ->
                val uid = user?.id ?: 0L
                repository.getNotificationsForUser(uid).collect { stateFlow.value = it }
            }
        }
    }

    val unreadNotificationsCount: StateFlow<Int> = MutableStateFlow(0).also { stateFlow ->
        viewModelScope.launch {
            currentUser.collect { user ->
                val uid = user?.id ?: 0L
                repository.getUnreadNotificationCount(uid).collect { stateFlow.value = it }
            }
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch { repository.markNotificationAsRead(id) }
    }

    fun markAllNotificationsAsRead() {
        val uid = currentUser.value?.id ?: 0L
        viewModelScope.launch { repository.markAllNotificationsAsRead(uid) }
    }

    // Google Sign-In helper
    fun loginWithGoogleAccount(email: String, name: String, avatar: String = "") {
        viewModelScope.launch {
            val user = repository.loginWithGoogle(email, name, "google-${System.currentTimeMillis()}", avatar)
            showToast("Welcome to UZair Gfx, ${user.fullName}!")
            if (_screenStack.value.any { it is Screen.Auth }) {
                navigateBack()
            }
        }
    }

    // Email Login
    fun loginWithEmail(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val res = repository.loginWithEmail(email, pass)
            res.fold(
                onSuccess = {
                    showToast("Logged in successfully as ${it.fullName}")
                    onSuccess()
                },
                onFailure = {
                    showToast(it.message ?: "Login failed")
                }
            )
        }
    }

    // Email Register
    fun registerWithEmail(name: String, email: String, pass: String, phone: String, cnic: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val res = repository.registerWithEmail(name, email, pass, phone, cnic)
            res.fold(
                onSuccess = {
                    showToast("Account created successfully!")
                    onSuccess()
                },
                onFailure = {
                    showToast(it.message ?: "Registration failed")
                }
            )
        }
    }

    fun logout() {
        repository.logout()
        showToast("Logged out successfully")
        resetTo(Screen.Home)
    }

    fun updateUserProfile(user: UserEntity) {
        viewModelScope.launch {
            repository.updateProfile(user)
            showToast("Profile updated successfully")
        }
    }

    // Application Submission
    fun submitApplication(application: ApplicationEntity, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val appNumber = repository.submitApplication(application)
            showToast("Application submitted! ID: $appNumber")
            onComplete(appNumber)
        }
    }

    // Service Request
    fun submitServiceRequest(type: String, details: String, phone: String, name: String) {
        val uid = currentUser.value?.id ?: 0L
        viewModelScope.launch {
            val reqNumber = repository.submitServiceRequest(
                ServiceRequestEntity(
                    requestId = "",
                    userId = uid,
                    applicantName = name,
                    serviceType = type,
                    details = details,
                    contactPhone = phone
                )
            )
            showToast("Request submitted! Reference: $reqNumber. Our team will contact you shortly.")
        }
    }

    // ==========================================
    // ADMIN ACTIONS & OBSERVABLES
    // ==========================================

    val allAdminOpportunities: StateFlow<List<OpportunityEntity>> = repository.getAllOpportunities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAdminApplications: StateFlow<List<ApplicationEntity>> = repository.getAllApplications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminActivityLogs = repository.getRecentActivityLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalUsersCount = repository.getTotalUsersCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalApplicationsCount = repository.getTotalApplicationsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingApplicationsCount = repository.getPendingApplicationsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val approvedApplicationsCount = repository.getApprovedApplicationsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalOpportunityCount = repository.getTotalOpportunityCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeOpportunityCount = repository.getActiveOpportunityCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun loginAdmin(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val res = repository.loginAdmin(email, pass)
            res.fold(
                onSuccess = {
                    showToast("Welcome Admin, ${it.fullName}")
                    onSuccess()
                },
                onFailure = {
                    showToast(it.message ?: "Admin verification failed")
                }
            )
        }
    }

    fun quickSuperAdminLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val admin = repository.quickLoginAdminAsSuperAdmin()
            showToast("Signed in as Super Administrator: ${admin.email}")
            onSuccess()
        }
    }

    fun logoutAdmin() {
        repository.logoutAdmin()
        showToast("Logged out of Admin Portal")
        navigateTo(Screen.Home)
    }

    fun saveOpportunity(opp: OpportunityEntity) {
        viewModelScope.launch {
            if (opp.id == 0L) {
                repository.insertOpportunity(opp, adminUser.value)
                showToast("Opportunity published successfully")
            } else {
                repository.updateOpportunity(opp, adminUser.value)
                showToast("Opportunity updated successfully")
            }
            navigateBack()
        }
    }

    fun deleteOpportunity(id: Long, title: String) {
        viewModelScope.launch {
            repository.deleteOpportunity(id, title, adminUser.value)
            showToast("Opportunity removed")
        }
    }

    fun togglePublishStatus(id: Long, currentStatus: String, title: String) {
        viewModelScope.launch {
            repository.togglePublishStatus(id, currentStatus, title, adminUser.value)
            showToast("Status updated")
        }
    }

    fun updateApplicationStatus(
        id: Long,
        appNum: String,
        newStatus: String,
        note: String,
        internalNote: String,
        applicantId: Long,
        oppTitle: String
    ) {
        viewModelScope.launch {
            repository.updateApplicationStatus(
                id, appNum, newStatus, note, internalNote, applicantId, oppTitle, adminUser.value
            )
            showToast("Application status changed to $newStatus")
        }
    }

    fun setUserBlocked(userId: Long, blocked: Boolean) {
        viewModelScope.launch {
            repository.setUserBlocked(userId, blocked, adminUser.value)
            showToast(if (blocked) "User blocked" else "User unblocked")
        }
    }

    fun updateUserRole(userId: Long, newRole: String) {
        viewModelScope.launch {
            repository.updateUserRole(userId, newRole, adminUser.value)
            showToast("Role changed to $newRole")
        }
    }

    fun sendBroadcastNotification(title: String, message: String, category: String) {
        viewModelScope.launch {
            repository.broadcastNotification(title, message, category, adminUser.value)
            showToast("Notification broadcast sent")
        }
    }

    fun createAnnouncement(ann: AnnouncementEntity) {
        viewModelScope.launch {
            repository.insertAnnouncement(ann, adminUser.value)
            showToast("Announcement published")
        }
    }

    fun deleteAnnouncement(id: Long) {
        viewModelScope.launch {
            repository.deleteAnnouncement(id)
            showToast("Announcement deleted")
        }
    }

    fun exportApplicationsCsv(): String {
        return repository.generateApplicationsCsv(allAdminApplications.value)
    }
}
