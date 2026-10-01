package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSurfaceVariant

@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showBorder: Boolean = true,
    isCircle: Boolean = false
) {
    val shape = if (isCircle) CircleShape else RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(Color.White)
            .then(
                if (showBorder) Modifier.border(1.dp, BrandSurfaceVariant, shape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_uzair_logo),
            contentDescription = "UZair Gfx Official Logo",
            modifier = Modifier
                .size(size * 0.85f)
                .clip(shape),
            contentScale = ContentScale.Fit
        )
    }
}
