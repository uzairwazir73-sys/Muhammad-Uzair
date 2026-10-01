package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomTab
import com.example.ui.components.UzairBottomNav
import com.example.ui.components.UzairTopBar
import com.example.ui.screens.admin.AdminApplicationReviewScreen
import com.example.ui.screens.admin.AdminApplicationsScreen
import com.example.ui.screens.admin.AdminContentScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.admin.AdminLogsScreen
import com.example.ui.screens.admin.AdminOpportunitiesScreen
import com.example.ui.screens.admin.AdminOpportunityEditorScreen
import com.example.ui.screens.admin.AdminSettingsScreen
import com.example.ui.screens.admin.AdminUsersScreen
import com.example.ui.screens.user.ApplicationFormScreen
import com.example.ui.screens.user.ApplicationReceiptScreen
import com.example.ui.screens.user.AuthScreen
import com.example.ui.screens.user.ExploreScreen
import com.example.ui.screens.user.FaqSupportScreen
import com.example.ui.screens.user.HomeScreen
import com.example.ui.screens.user.MyApplicationsScreen
import com.example.ui.screens.user.NotificationsScreen
import com.example.ui.screens.user.OnlineServicesScreen
import com.example.ui.screens.user.OpportunityDetailScreen
import com.example.ui.screens.user.SavedOpportunitiesScreen
import com.example.ui.screens.user.SplashScreen
import com.example.ui.screens.user.UserProfileScreen
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FilterState
import com.example.ui.viewmodel.PortalViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(viewModel: PortalViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val adminUser by viewModel.adminUser.collectAsStateWithLifecycle()

    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val filteredOpportunities by viewModel.filteredOpportunities.collectAsStateWithLifecycle()
    val featuredOpportunities by viewModel.featuredOpportunities.collectAsStateWithLifecycle()
    val publishedOpportunities by viewModel.publishedOpportunities.collectAsStateWithLifecycle()
    val savedIds by viewModel.savedOpportunityIds.collectAsStateWithLifecycle()
    val myApplications by viewModel.myApplications.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val faqs by viewModel.faqs.collectAsStateWithLifecycle()
    val notifications by viewModel.userNotifications.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Observe toast notifications from ViewModel
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Determine current bottom navigation tab
    var currentBottomTab by remember { mutableStateOf(BottomTab.HOME) }
    LaunchedEffect(currentScreen) {
        when (currentScreen) {
            is Screen.Home -> currentBottomTab = BottomTab.HOME
            is Screen.Explore -> currentBottomTab = BottomTab.EXPLORE
            is Screen.MyApplications -> currentBottomTab = BottomTab.APPLICATIONS
            is Screen.SavedOpportunities -> currentBottomTab = BottomTab.SAVED
            is Screen.Profile -> currentBottomTab = BottomTab.PROFILE
            else -> {}
        }
    }

    val isUserMainScreen = currentScreen is Screen.Home ||
        currentScreen is Screen.Explore ||
        currentScreen is Screen.MyApplications ||
        currentScreen is Screen.SavedOpportunities ||
        currentScreen is Screen.Profile

    val isAdminScreen = currentScreen is Screen.AdminDashboard ||
        currentScreen is Screen.AdminOpportunities ||
        currentScreen is Screen.AdminOpportunityEditor ||
        currentScreen is Screen.AdminApplications ||
        currentScreen is Screen.AdminApplicationReview ||
        currentScreen is Screen.AdminUsers ||
        currentScreen is Screen.AdminContent ||
        currentScreen is Screen.AdminLogs ||
        currentScreen is Screen.AdminSettings ||
        currentScreen is Screen.AdminLogin

    // BackHandler: handle custom stack navigation
    val canGoBack = currentScreen !is Screen.Splash && currentScreen !is Screen.Home
    BackHandler(enabled = canGoBack) {
        if (!viewModel.navigateBack()) {
            viewModel.resetTo(Screen.Home)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (!isAdminScreen && currentScreen !is Screen.Splash && currentScreen !is Screen.Auth) {
                UzairTopBar(
                    canNavigateBack = !isUserMainScreen,
                    unreadNotifications = unreadNotifications,
                    onBackClick = {
                        if (!viewModel.navigateBack()) {
                            viewModel.resetTo(Screen.Home)
                        }
                    },
                    onNotificationClick = { viewModel.navigateTo(Screen.Notifications) },
                    onAdminClick = {
                        if (adminUser != null) {
                            viewModel.navigateTo(Screen.AdminDashboard)
                        } else {
                            viewModel.navigateTo(Screen.AdminLogin)
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isUserMainScreen) {
                UzairBottomNav(
                    currentTab = currentBottomTab,
                    onTabSelected = { tab ->
                        currentBottomTab = tab
                        when (tab) {
                            BottomTab.HOME -> viewModel.navigateTo(Screen.Home)
                            BottomTab.EXPLORE -> viewModel.navigateTo(Screen.Explore)
                            BottomTab.APPLICATIONS -> {
                                if (currentUser == null) {
                                    viewModel.navigateTo(Screen.Auth)
                                } else {
                                    viewModel.navigateTo(Screen.MyApplications)
                                }
                            }
                            BottomTab.SAVED -> viewModel.navigateTo(Screen.SavedOpportunities)
                            BottomTab.PROFILE -> viewModel.navigateTo(Screen.Profile)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Splash -> {
                    SplashScreen(
                        onTimeout = {
                            viewModel.resetTo(Screen.Home)
                        }
                    )
                }

                is Screen.Auth -> {
                    AuthScreen(
                        onGoogleLogin = { email, name ->
                            viewModel.loginWithGoogleAccount(email, name)
                        },
                        onEmailLogin = { email, pass ->
                            viewModel.loginWithEmail(email, pass) {
                                viewModel.navigateBack()
                            }
                        },
                        onEmailRegister = { name, email, pass, phone, cnic ->
                            viewModel.registerWithEmail(name, email, pass, phone, cnic) {
                                viewModel.navigateBack()
                            }
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.Home -> {
                    HomeScreen(
                        currentUser = currentUser,
                        featuredOpportunities = featuredOpportunities,
                        latestOpportunities = publishedOpportunities,
                        announcements = announcements,
                        savedIds = savedIds,
                        onCategoryClick = { cat ->
                            viewModel.filterState.value = FilterState(category = cat)
                            viewModel.navigateTo(Screen.Explore)
                        },
                        onOpportunityClick = { oppId ->
                            viewModel.navigateTo(Screen.OpportunityDetail(oppId))
                        },
                        onSaveClick = { oppId ->
                            viewModel.toggleSaveOpportunity(oppId)
                        },
                        onApplyClick = { oppId ->
                            viewModel.navigateTo(Screen.ApplicationForm(oppId))
                        },
                        onSearchSubmit = { q ->
                            viewModel.searchQuery.value = q
                            viewModel.navigateTo(Screen.Explore)
                        },
                        onExploreAll = { viewModel.navigateTo(Screen.Explore) },
                        onOnlineServicesClick = { viewModel.navigateTo(Screen.OnlineServices) },
                        onFaqClick = { viewModel.navigateTo(Screen.FaqSupport) }
                    )
                }

                is Screen.Explore -> {
                    ExploreScreen(
                        opportunities = filteredOpportunities,
                        searchQuery = searchQuery,
                        filterState = filterState,
                        savedIds = savedIds,
                        onSearchChange = { viewModel.searchQuery.value = it },
                        onCategorySelect = { cat ->
                            viewModel.filterState.value = viewModel.filterState.value.copy(category = cat)
                        },
                        onFilterApply = { newState ->
                            viewModel.filterState.value = newState
                        },
                        onOpportunityClick = { oppId ->
                            viewModel.navigateTo(Screen.OpportunityDetail(oppId))
                        },
                        onSaveClick = { oppId ->
                            viewModel.toggleSaveOpportunity(oppId)
                        },
                        onApplyClick = { oppId ->
                            viewModel.navigateTo(Screen.ApplicationForm(oppId))
                        }
                    )
                }

                is Screen.OpportunityDetail -> {
                    val opp = publishedOpportunities.find { it.id == screen.id }
                        ?: viewModel.allAdminOpportunities.collectAsStateWithLifecycle().value.find { it.id == screen.id }
                    if (opp != null) {
                        OpportunityDetailScreen(
                            opportunity = opp,
                            isSaved = savedIds.contains(opp.id),
                            onBackClick = { viewModel.navigateBack() },
                            onSaveToggle = { viewModel.toggleSaveOpportunity(opp.id) },
                            onApplyClick = { viewModel.navigateTo(Screen.ApplicationForm(opp.id)) }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                is Screen.ApplicationForm -> {
                    val opp = publishedOpportunities.find { it.id == screen.opportunityId }
                        ?: viewModel.allAdminOpportunities.collectAsStateWithLifecycle().value.find { it.id == screen.opportunityId }
                    if (opp != null) {
                        ApplicationFormScreen(
                            opportunity = opp,
                            currentUser = currentUser,
                            onBackClick = { viewModel.navigateBack() },
                            onSubmitSuccess = { appEntity ->
                                viewModel.submitApplication(appEntity) { appNumber ->
                                    viewModel.navigateTo(Screen.ApplicationReceipt(appNumber))
                                }
                            }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                is Screen.ApplicationReceipt -> {
                    ApplicationReceiptScreen(
                        applicationNumber = screen.appNumber,
                        onTrackClick = {
                            viewModel.resetTo(Screen.Home)
                            viewModel.navigateTo(Screen.MyApplications)
                        },
                        onHomeClick = {
                            viewModel.resetTo(Screen.Home)
                        }
                    )
                }

                is Screen.MyApplications -> {
                    MyApplicationsScreen(
                        applications = myApplications,
                        onExploreClick = { viewModel.navigateTo(Screen.Explore) }
                    )
                }

                is Screen.SavedOpportunities -> {
                    val savedOpps = publishedOpportunities.filter { savedIds.contains(it.id) }
                    SavedOpportunitiesScreen(
                        savedOpportunities = savedOpps,
                        onOpportunityClick = { oppId ->
                            viewModel.navigateTo(Screen.OpportunityDetail(oppId))
                        },
                        onSaveToggle = { oppId ->
                            viewModel.toggleSaveOpportunity(oppId)
                        },
                        onApplyClick = { oppId ->
                            viewModel.navigateTo(Screen.ApplicationForm(oppId))
                        },
                        onExploreClick = { viewModel.navigateTo(Screen.Explore) }
                    )
                }

                is Screen.Notifications -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onNotificationClick = { notif ->
                            viewModel.markNotificationAsRead(notif.id)
                        },
                        onMarkAllRead = {
                            viewModel.markAllNotificationsAsRead()
                        }
                    )
                }

                is Screen.Profile -> {
                    UserProfileScreen(
                        user = currentUser,
                        onUpdateProfile = { updated ->
                            viewModel.updateUserProfile(updated)
                        },
                        onLogout = {
                            viewModel.logout()
                        },
                        onLoginClick = {
                            viewModel.navigateTo(Screen.Auth)
                        }
                    )
                }

                is Screen.OnlineServices -> {
                    OnlineServicesScreen(
                        currentUser = currentUser,
                        onBackClick = { viewModel.navigateBack() },
                        onSubmitRequest = { type, details, phone, name ->
                            viewModel.submitServiceRequest(type, details, phone, name)
                        }
                    )
                }

                is Screen.FaqSupport -> {
                    FaqSupportScreen(
                        faqs = faqs,
                        onBackClick = { viewModel.navigateBack() },
                        onSubmitTicket = { subject, message ->
                            viewModel.showToast("Support ticket disptached. Reference #TK-${(1000..9999).random()}")
                        }
                    )
                }

                // ===================================
                // ADMIN SCREENS
                // ===================================

                is Screen.AdminLogin -> {
                    AdminLoginScreen(
                        onAdminLogin = { email, pass ->
                            viewModel.loginAdmin(email, pass) {
                                viewModel.navigateTo(Screen.AdminDashboard)
                            }
                        },
                        onQuickSuperAdmin = {
                            viewModel.quickSuperAdminLogin {
                                viewModel.navigateTo(Screen.AdminDashboard)
                            }
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminDashboard -> {
                    val totalUsers by viewModel.totalUsersCount.collectAsStateWithLifecycle()
                    val totalApps by viewModel.totalApplicationsCount.collectAsStateWithLifecycle()
                    val pendingApps by viewModel.pendingApplicationsCount.collectAsStateWithLifecycle()
                    val approvedApps by viewModel.approvedApplicationsCount.collectAsStateWithLifecycle()
                    val totalOpps by viewModel.totalOpportunityCount.collectAsStateWithLifecycle()
                    val activeOpps by viewModel.activeOpportunityCount.collectAsStateWithLifecycle()
                    val recentLogs by viewModel.adminActivityLogs.collectAsStateWithLifecycle()

                    AdminDashboardScreen(
                        adminUser = adminUser,
                        totalUsers = totalUsers,
                        totalApplications = totalApps,
                        pendingApplications = pendingApps,
                        approvedApplications = approvedApps,
                        totalOpportunities = totalOpps,
                        activeOpportunities = activeOpps,
                        recentLogs = recentLogs,
                        onNavigateOpportunities = { viewModel.navigateTo(Screen.AdminOpportunities) },
                        onNavigateApplications = { viewModel.navigateTo(Screen.AdminApplications) },
                        onNavigateUsers = { viewModel.navigateTo(Screen.AdminUsers) },
                        onNavigateContent = { viewModel.navigateTo(Screen.AdminContent) },
                        onNavigateLogs = { viewModel.navigateTo(Screen.AdminLogs) },
                        onNavigateSettings = { viewModel.navigateTo(Screen.AdminSettings) },
                        onAddNewOpportunity = { viewModel.navigateTo(Screen.AdminOpportunityEditor(0L)) },
                        onExportCsv = { viewModel.navigateTo(Screen.AdminSettings) },
                        onLogout = { viewModel.logoutAdmin() },
                        onExitAdmin = { viewModel.resetTo(Screen.Home) }
                    )
                }

                is Screen.AdminOpportunities -> {
                    val allAdminOpps by viewModel.allAdminOpportunities.collectAsStateWithLifecycle()
                    AdminOpportunitiesScreen(
                        adminUser = adminUser,
                        opportunities = allAdminOpps,
                        onAddNew = { viewModel.navigateTo(Screen.AdminOpportunityEditor(0L)) },
                        onEdit = { oppId -> viewModel.navigateTo(Screen.AdminOpportunityEditor(oppId)) },
                        onDelete = { oppId, title -> viewModel.deleteOpportunity(oppId, title) },
                        onTogglePublish = { oppId, st, title -> viewModel.togglePublishStatus(oppId, st, title) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminOpportunityEditor -> {
                    val allAdminOpps by viewModel.allAdminOpportunities.collectAsStateWithLifecycle()
                    val targetOpp = if (screen.opportunityId > 0) allAdminOpps.find { it.id == screen.opportunityId } else null

                    AdminOpportunityEditorScreen(
                        adminUser = adminUser,
                        existingOpportunity = targetOpp,
                        onSave = { opp -> viewModel.saveOpportunity(opp) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminApplications -> {
                    val allAdminApps by viewModel.allAdminApplications.collectAsStateWithLifecycle()
                    AdminApplicationsScreen(
                        adminUser = adminUser,
                        applications = allAdminApps,
                        onReviewApplication = { appId ->
                            viewModel.navigateTo(Screen.AdminApplicationReview(appId))
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminApplicationReview -> {
                    val allAdminApps by viewModel.allAdminApplications.collectAsStateWithLifecycle()
                    val targetApp = allAdminApps.find { it.id == screen.applicationId }
                    if (targetApp != null) {
                        AdminApplicationReviewScreen(
                            adminUser = adminUser,
                            application = targetApp,
                            onUpdateStatus = { newStatus, feedback, internalNote ->
                                viewModel.updateApplicationStatus(
                                    targetApp.id,
                                    targetApp.applicationNumber,
                                    newStatus,
                                    feedback,
                                    internalNote,
                                    targetApp.userId,
                                    targetApp.opportunityTitle
                                )
                                viewModel.navigateBack()
                            },
                            onBackClick = { viewModel.navigateBack() }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                is Screen.AdminUsers -> {
                    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
                    AdminUsersScreen(
                        adminUser = adminUser,
                        users = allUsers,
                        onToggleBlock = { uid, blk -> viewModel.setUserBlocked(uid, !blk) },
                        onRoleChange = { uid, role -> viewModel.updateUserRole(uid, role) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminContent -> {
                    AdminContentScreen(
                        adminUser = adminUser,
                        announcements = announcements,
                        onCreateAnnouncement = { ann -> viewModel.createAnnouncement(ann) },
                        onDeleteAnnouncement = { id -> viewModel.deleteAnnouncement(id) },
                        onBroadcastNotification = { t, m, c -> viewModel.sendBroadcastNotification(t, m, c) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminLogs -> {
                    val logs by viewModel.adminActivityLogs.collectAsStateWithLifecycle()
                    AdminLogsScreen(
                        adminUser = adminUser,
                        logs = logs,
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                is Screen.AdminSettings -> {
                    AdminSettingsScreen(
                        adminUser = adminUser,
                        onExportCsv = { viewModel.exportApplicationsCsv() },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                else -> {}
            }
        }
    }
}
