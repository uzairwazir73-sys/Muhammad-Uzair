package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryContainer
import com.example.ui.theme.BrandSurface
import com.example.ui.theme.BrandSurfaceVariant
import com.example.ui.theme.LogoRedAccent
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorContainer
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminUsersScreen(
    adminUser: UserEntity?,
    users: List<UserEntity>,
    onToggleBlock: (userId: Long, currentBlocked: Boolean) -> Unit,
    onRoleChange: (userId: Long, newRole: String) -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = users.filter { u ->
        searchQuery.isBlank() ||
            u.fullName.contains(searchQuery, ignoreCase = true) ||
            u.email.contains(searchQuery, ignoreCase = true) ||
            u.cnic.contains(searchQuery, ignoreCase = true) ||
            u.phone.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("admin_users_screen")
    ) {
        AdminTopBar(
            title = "User Management",
            adminUser = adminUser,
            onBackClick = onBackClick,
            onLogoutClick = onBackClick
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BrandSurface,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search citizens by name, email, CNIC, phone...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${filtered.size} Registered Citizens",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextSecondary)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered) { user ->
                AdminUserCard(
                    user = user,
                    isSuperAdmin = adminUser?.role == "SUPER_ADMIN",
                    onToggleBlock = { onToggleBlock(user.id, user.isBlocked) },
                    onRoleChange = { newRole -> onRoleChange(user.id, newRole) }
                )
            }
        }
    }
}

@Composable
fun AdminUserCard(
    user: UserEntity,
    isSuperAdmin: Boolean,
    onToggleBlock: () -> Unit,
    onRoleChange: (String) -> Unit
) {
    val createdStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(user.createdAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (user.role != "USER") BrandPrimaryContainer else BrandSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.fullName.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = user.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = user.email, color = TextSecondary, fontSize = 11.5.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (user.isBlocked) StatusErrorContainer else StatusSuccessContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (user.isBlocked) "BLOCKED" else "ACTIVE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (user.isBlocked) StatusError else StatusSuccess,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "CNIC: ${if (user.cnic.isNotBlank()) user.cnic else "Not Provided"}", fontSize = 11.5.sp, color = TextSecondary)
                Text(text = "Role: ${user.role}", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = BrandPrimary)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Phone: ${if (user.phone.isNotBlank()) user.phone else "—"}", fontSize = 11.5.sp, color = TextSecondary)
                Text(text = "Joined: $createdStr", fontSize = 11.sp, color = TextMuted)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Admin Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSuperAdmin && user.email != "uzaircomputer73@gmail.com") {
                    // Role changer button
                    OutlinedButton(
                        onClick = {
                            val nextRole = when (user.role) {
                                "USER" -> "APPLICATION_MANAGER"
                                "APPLICATION_MANAGER" -> "SUPER_ADMIN"
                                else -> "USER"
                            }
                            onRoleChange(nextRole)
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Role: ${user.role}", fontSize = 11.sp)
                    }
                }

                if (user.email != "uzaircomputer73@gmail.com") {
                    OutlinedButton(
                        onClick = onToggleBlock,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (user.isBlocked) StatusSuccess else LogoRedAccent
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(
                            imageVector = if (user.isBlocked) Icons.Default.LockOpen else Icons.Default.Block,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (user.isBlocked) "Unblock" else "Block User", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
