package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

/**
 * High-fidelity Codiffera brand logo with glowing splash animation on startup,
 * electric blue & neon cyan gradients, dynamic pulse ring, and futuristic typography.
 */
@Composable
fun CodifferaLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    showGlow: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CodifferaGlow")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    Box(
        modifier = modifier
            .size(size)
            .testTag("codiffera_logo_badge"),
        contentAlignment = Alignment.Center
    ) {
        if (showGlow) {
            // Ambient neon pulse ring
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(pulseScale)
            ) {
                val radius = this.size.minDimension / 2f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            StudioCyanPrimary.copy(alpha = glowAlpha * 0.45f),
                            StudioNeonBlue.copy(alpha = glowAlpha * 0.2f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = radius * 1.3f
                    ),
                    radius = radius * 1.3f
                )
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            StudioCyanPrimary.copy(alpha = 0.8f),
                            StudioNeonBlue.copy(alpha = 0.3f),
                            StudioCyanGlow.copy(alpha = 0.9f),
                            StudioCyanPrimary.copy(alpha = 0.8f)
                        ),
                        center = center
                    ),
                    radius = radius * 0.95f,
                    style = Stroke(width = 1.8.dp.toPx())
                )
            }
        }

        // Geometric stylized Codiffera Icon container
        Box(
            modifier = Modifier
                .size(size * 0.82f)
                .clip(RoundedCornerShape(size * 0.24f))
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF0D1B2A), Color(0xFF050B14))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(StudioCyanPrimary, StudioNeonBlue.copy(alpha = 0.4f))
                    ),
                    shape = RoundedCornerShape(size * 0.24f)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Render the stylized Codiffera Logo Icon (C-arrow forward motion)
            CodifferaVectorIcon(modifier = Modifier.size(size * 0.62f))
        }
    }
}

/**
 * Geometric vector representation of the Codiffera symbol:
 * A stylized circular 'C' curve in electric blue and cyan gradient with forward data blocks and directional arrow.
 */
@Composable
fun CodifferaVectorIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // C-shape arc path
        val cPath = Path().apply {
            moveTo(w * 0.55f, h * 0.15f)
            cubicTo(w * 0.25f, h * 0.15f, w * 0.10f, h * 0.32f, w * 0.10f, h * 0.50f)
            cubicTo(w * 0.10f, h * 0.68f, w * 0.25f, h * 0.85f, w * 0.55f, h * 0.85f)
            lineTo(w * 0.45f, h * 0.70f)
            cubicTo(w * 0.30f, h * 0.70f, w * 0.24f, h * 0.60f, w * 0.24f, h * 0.50f)
            cubicTo(w * 0.24f, h * 0.40f, w * 0.30f, h * 0.30f, w * 0.45f, h * 0.30f)
            close()
        }

        drawPath(
            path = cPath,
            brush = Brush.verticalGradient(
                listOf(StudioCyanPrimary, StudioNeonBlue, Color(0xFF0052CC))
            )
        )

        // Middle data blocks (horizontal bars)
        val block1 = Path().apply {
            moveTo(w * 0.42f, h * 0.38f)
            lineTo(w * 0.62f, h * 0.38f)
            lineTo(w * 0.58f, h * 0.46f)
            lineTo(w * 0.38f, h * 0.46f)
            close()
        }
        drawPath(
            path = block1,
            brush = Brush.horizontalGradient(
                listOf(StudioCyanGlow, StudioCyanPrimary)
            )
        )

        val block2 = Path().apply {
            moveTo(w * 0.46f, h * 0.52f)
            lineTo(w * 0.66f, h * 0.52f)
            lineTo(w * 0.62f, h * 0.60f)
            lineTo(w * 0.42f, h * 0.60f)
            close()
        }
        drawPath(
            path = block2,
            brush = Brush.horizontalGradient(
                listOf(StudioNeonBlue, StudioCyanPrimary)
            )
        )

        // Forward Arrow Pointing Right
        val arrow = Path().apply {
            moveTo(w * 0.62f, h * 0.32f)
            lineTo(w * 0.92f, h * 0.50f)
            lineTo(w * 0.62f, h * 0.68f)
            lineTo(w * 0.66f, h * 0.50f)
            close()
        }
        drawPath(
            path = arrow,
            brush = Brush.linearGradient(
                listOf(StudioCyanPrimary, StudioNeonBlue),
                start = Offset(w * 0.62f, h * 0.50f),
                end = Offset(w * 0.92f, h * 0.50f)
            )
        )
    }
}

/**
 * Full Prominent Header Banner with Glowing Splash Animation & Brand Typography
 */
@Composable
fun CodifferaHeroBanner(
    modifier: Modifier = Modifier,
    subtitle: String = "DEV TOOL STUDIO",
    onExploreClicked: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CodifferaSplash")
    
    // Ambient ripple animation representing app startup splash glow
    val splashGlowRadius by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splashGlowRadius"
    )

    val splashAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splashAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("codiffera_hero_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFF090D16)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    StudioCyanPrimary.copy(alpha = 0.4f),
                    StudioNeonBlue.copy(alpha = 0.6f),
                    StudioBorderDark
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glowing splash backdrop canvas
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .scale(splashGlowRadius)
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            StudioCyanPrimary.copy(alpha = splashAlpha * 0.28f),
                            StudioNeonBlue.copy(alpha = splashAlpha * 0.12f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = size.width * 0.45f
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Prominent reference logo with active pulsing halo
                Box(
                    modifier = Modifier.size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Pulsing startup splash halo
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(splashGlowRadius)
                    ) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    StudioCyanPrimary.copy(alpha = splashAlpha * 0.5f),
                                    StudioCyanGlow.copy(alpha = 0.2f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.minDimension / 2f
                            )
                        )
                    }

                    // Logo Icon Frame
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0B1528), Color(0xFF030710))
                                )
                            )
                            .border(
                                1.5.dp,
                                Brush.linearGradient(
                                    listOf(StudioCyanPrimary, StudioNeonBlue)
                                ),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CodifferaVectorIcon(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Codiffera Wordmark with Neon Cyan & Electric Blue Gradient
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Codiff",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "era",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        color = StudioCyanPrimary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Studio Subtitle with tracking
                Text(
                    text = subtitle,
                    color = StudioCyanPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // High-fidelity Developer Tool Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .background(Color(0xFF131E30), RoundedCornerShape(20.dp))
                        .border(1.dp, StudioCyanPrimary.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StudioCyanPrimary)
                    )
                    Text(
                        text = "AUTONOMOUS SYNTHESIS ENGINE • READY",
                        color = StudioTextCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}
