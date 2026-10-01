package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusInfoContainer
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OfficialDisclaimerCard(
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(StatusInfoContainer.copy(alpha = 0.5f))
            .border(1.dp, StatusInfo.copy(alpha = 0.3f), shape)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = "Verified Notice",
                tint = StatusInfo,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Official Information & Verification Notice",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StatusInfo,
                    fontSize = 13.sp
                )
            )
        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = "Please verify important admission, job, scholarship, Hajj and Umrah information from the official organization before submitting an application or making any payment. UZair Gfx is an information and application assistance platform.",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 11.5.sp,
                lineHeight = 16.sp
            )
        )
    }
}
