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
 * Register ekranı — kullanıcı kayıt formu.
 */
@Composable
fun RegisterScreen(
    state: AuthState,
    onRegister: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onClearError: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
        // Dekoratif arka plan
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-40).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GradientEnd.copy(alpha = 0.12f),
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

            // Başlık
            Text(
                text = "✨",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Hesap Oluşturun",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hemen ücretsiz kaydolun",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Register formu
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        onClearError()
                    },
                    label = "Kullanıcı Adı (min 3 karakter)",
                    isError = state.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        onClearError()
                    },
                    label = "Şifre (min 6 karakter)",
                    isPassword = true,
                    isError = state.errorMessage != null
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        onClearError()
                    },
                    label = "Şifre Tekrar",
                    isPassword = true,
                    isError = state.errorMessage != null,
                    onImeAction = { onRegister(username, password, confirmPassword) }
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
                    text = "Kayıt Ol",
                    onClick = { onRegister(username, password, confirmPassword) },
                    isLoading = state.isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login linki
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Zaten hesabınız var mı? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Giriş Yapın",
                    style = MaterialTheme.typography.labelLarge,
                    color = Purple80,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
