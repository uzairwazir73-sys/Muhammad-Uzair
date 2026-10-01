package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

enum class BottomTab {
    HOME, EXPLORE, APPLICATIONS, SAVED, PROFILE
}

@Composable
fun UzairBottomNav(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    NavigationBar(
        containerColor = BrandSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == BottomTab.HOME,
            onClick = { onTabSelected(BottomTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == BottomTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            modifier = Modifier.testTag("nav_item_home"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimaryContainer,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = currentTab == BottomTab.EXPLORE,
            onClick = { onTabSelected(BottomTab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == BottomTab.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                    contentDescription = "Explore"
                )
            },
            label = { Text("Explore") },
            modifier = Modifier.testTag("nav_item_explore"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimaryContainer,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = currentTab == BottomTab.APPLICATIONS,
            onClick = { onTabSelected(BottomTab.APPLICATIONS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == BottomTab.APPLICATIONS) Icons.Filled.Assignment else Icons.Outlined.Assignment,
                    contentDescription = "Applications"
                )
            },
            label = { Text("Apply") },
            modifier = Modifier.testTag("nav_item_applications"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimaryContainer,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = currentTab == BottomTab.SAVED,
            onClick = { onTabSelected(BottomTab.SAVED) },
            icon = {
                Icon(
                    imageVector = if (currentTab == BottomTab.SAVED) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Saved"
                )
            },
            label = { Text("Saved") },
            modifier = Modifier.testTag("nav_item_saved"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimaryContainer,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = currentTab == BottomTab.PROFILE,
            onClick = { onTabSelected(BottomTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == BottomTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            modifier = Modifier.testTag("nav_item_profile"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimaryContainer,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )
    }
}
