package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UzairTopBar(
    title: String = "UZair Gfx",
    subtitle: String = "Online Apply & Information Portal",
    canNavigateBack: Boolean = false,
    unreadNotifications: Int = 0,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAdminClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BrandSurface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (canNavigateBack) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrandPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            } else {
                BrandLogo(
                    size = 40.dp,
                    showBorder = true,
                    modifier = Modifier.testTag("brand_logo_header")
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = canNavigateBack) { onBackClick() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Notification Bell with Badge
            BadgedBox(
                badge = {
                    if (unreadNotifications > 0) {
                        Badge(
                            containerColor = LogoRedAccent,
                            contentColor = Color.White
                        ) {
                            Text(text = if (unreadNotifications > 9) "9+" else "$unreadNotifications")
                        }
                    }
                }
            ) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.testTag("header_notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = BrandPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Admin Portal Access Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(BrandPrimary.copy(alpha = 0.1f))
                    .clickable { onAdminClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("header_admin_portal_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Portal",
                        tint = BrandPrimaryDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Admin",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = BrandPrimaryDark
                        )
                    )
                }
            }
        }
    }
}
