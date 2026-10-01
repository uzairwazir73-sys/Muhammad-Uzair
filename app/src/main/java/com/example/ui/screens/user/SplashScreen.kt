package com.example.ui.screens.user

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandLogo
import com.example.ui.theme.BrandBackground
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 800)
        )
        delay(1200)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .testTag("splash_screen_root"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            BrandLogo(
                size = 110.dp,
                showBorder = true,
                modifier = Modifier.testTag("splash_logo")
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "UZair Gfx",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = BrandPrimary,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Online Apply & Information Portal",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = BrandPrimary,
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
