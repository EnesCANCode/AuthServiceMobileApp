package dev.enes.authapp.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.enes.authapp.getPlatformName
import dev.enes.authapp.ui.auth.AuthState
import dev.enes.authapp.ui.components.GlassCard
import dev.enes.authapp.ui.theme.*

/**
 * Home ekranı — giriş yapan kullanıcıyı karşılar.
 * Platform bilgisi, profil ve çıkış yap butonu içerir.
 */
@Composable
fun HomeScreen(
    state: AuthState,
    onLogout: () -> Unit,
    onRefreshToken: () -> Unit
) {
    // Animasyonlu glow efekti
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        DarkSurface,
                        DarkBackground
                    )
                )
            )
    ) {
        // Animated glow arka plan
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-100).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Purple80.copy(alpha = 0.08f * glowAlpha),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Profil avatarı
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Purple80.copy(alpha = glowAlpha),
                                GradientEnd.copy(alpha = glowAlpha)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (state.username?.firstOrNull()?.uppercase() ?: "?"),
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Kullanıcı adı
            Text(
                text = "Merhaba, ${state.username ?: "Kullanıcı"} 👋",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Oturumunuz aktif",
                style = MaterialTheme.typography.bodyMedium,
                color = SuccessGreen
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Platform bilgisi kartı
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "📱 Cihaz Bilgisi",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                InfoRow(label = "Platform", value = getPlatformName())
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = "Client Tipi", value = "Mobile")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = "Token Tipi", value = "Bearer (JWT)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Token yönetimi kartı
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🔑 Oturum Yönetimi",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Token yenile butonu
                OutlinedButton(
                    onClick = onRefreshToken,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Purple80
                    ),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Purple80.copy(alpha = 0.5f), GradientEnd.copy(alpha = 0.5f))
                        )
                    )
                ) {
                    Text("🔄  Token Yenile", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Çıkış yap butonu
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed.copy(alpha = 0.15f),
                        contentColor = ErrorRed
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = ErrorRed,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("🚪  Çıkış Yap", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

/**
 * Bilgi satırı bileşeni.
 */
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}
