package dev.enes.authapp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import dev.enes.authapp.ui.auth.*
import dev.enes.authapp.ui.home.HomeScreen
import dev.enes.authapp.ui.theme.AuthAppTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

/**
 * Ekran navigasyonu için sealed class.
 */
sealed class Screen {
    data object Login : Screen()
    data object Register : Screen()
    data object Home : Screen()
}

/**
 * Root Composable — uygulama giriş noktası.
 * Koin ViewModel ile state yönetimi, basit state-based navigation.
 */
@Composable
fun App() {
    AuthAppTheme {
        val viewModel: AuthViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()

        // Başlangıç ekranını oturum durumuna göre belirle
        var currentScreen by remember {
            mutableStateOf<Screen>(
                if (state.isAuthenticated) Screen.Home else Screen.Login
            )
        }

        // ViewModel event'lerini dinle (navigation, error)
        LaunchedEffect(Unit) {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AuthEvent.NavigateToHome -> currentScreen = Screen.Home
                    is AuthEvent.NavigateToLogin -> currentScreen = Screen.Login
                    is AuthEvent.ShowError -> { /* error state'de gösteriliyor */ }
                }
            }
        }

        // Animasyonlu ekran geçişi
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(tween(300)) + slideInHorizontally(
                    initialOffsetX = { if (targetState is Screen.Home) it else -it },
                    animationSpec = tween(300)
                ) togetherWith fadeOut(tween(200)) + slideOutHorizontally(
                    targetOffsetX = { if (targetState is Screen.Home) -it else it },
                    animationSpec = tween(200)
                )
            }
        ) { screen ->
            when (screen) {
                Screen.Login -> LoginScreen(
                    state = state,
                    onLogin = { username, password ->
                        viewModel.login(username, password)
                    },
                    onNavigateToRegister = {
                        viewModel.clearError()
                        currentScreen = Screen.Register
                    },
                    onClearError = { viewModel.clearError() }
                )

                Screen.Register -> RegisterScreen(
                    state = state,
                    onRegister = { username, password, confirmPassword ->
                        viewModel.register(username, password, confirmPassword)
                    },
                    onNavigateToLogin = {
                        viewModel.clearError()
                        currentScreen = Screen.Login
                    },
                    onClearError = { viewModel.clearError() }
                )

                Screen.Home -> HomeScreen(
                    state = state,
                    onLogout = { viewModel.logout() },
                    onRefreshToken = { viewModel.refreshToken() }
                )
            }
        }
    }
}
