package dev.enes.authapp.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.enes.authapp.ui.components.*
import dev.enes.authapp.ui.theme.*

/**
 * Login ekranı — premium glassmorphism tasarım.
 */
@Composable
fun LoginScreen(
    state: AuthState,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onClearError: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
        // Dekoratif arka plan gradient dairesi
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-50).dp, y = (-80).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Purple80.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Pink80.copy(alpha = 0.1f),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo / Başlık
            Text(
                text = "🔐",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Hoş Geldiniz",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hesabınıza giriş yapın",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Login formu
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        onClearError()
                    },
                    label = "Kullanıcı Adı",
                    isError = state.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        onClearError()
                    },
                    label = "Şifre",
                    isPassword = true,
                    isError = state.errorMessage != null,
                    onImeAction = { onLogin(username, password) }
                )

                // Error mesajı
                AnimatedVisibility(
                    visible = state.errorMessage != null,
                    enter = fadeIn(tween(200)) + expandVertically(),
                    exit = fadeOut(tween(200)) + shrinkVertically()
                ) {
                    state.errorMessage?.let { error ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = error,
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = "Giriş Yap",
                    onClick = { onLogin(username, password) },
                    isLoading = state.isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register linki
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hesabınız yok mu? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Kayıt Olun",
                    style = MaterialTheme.typography.labelLarge,
                    color = Purple80,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
