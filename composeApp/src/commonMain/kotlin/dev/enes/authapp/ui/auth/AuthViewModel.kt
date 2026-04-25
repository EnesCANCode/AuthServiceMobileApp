package dev.enes.authapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.enes.authapp.domain.model.NetworkResult
import dev.enes.authapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Auth UI state — MVI pattern.
 */
data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val username: String? = null,
    val errorMessage: String? = null
)

/**
 * Tek seferlik UI olayları (navigation, snackbar vb.)
 */
sealed class AuthEvent {
    data object NavigateToHome : AuthEvent()
    data object NavigateToLogin : AuthEvent()
    data class ShowError(val message: String) : AuthEvent()
}

/**
 * Auth ViewModel — MVI mimarisi.
 * Login, register, logout ve token refresh işlemlerini yönetir.
 */
class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    init {
        // Uygulama açıldığında oturum kontrolü
        checkAuthState()
    }

    private fun checkAuthState() {
        if (repository.isLoggedIn()) {
            _state.value = AuthState(
                isAuthenticated = true,
                username = repository.getUsername()
            )
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Kullanıcı adı ve şifre boş olamaz")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.login(username, password)) {
                is NetworkResult.Success -> {
                    _state.value = AuthState(
                        isAuthenticated = true,
                        username = username
                    )
                    _events.emit(AuthEvent.NavigateToHome)
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    _events.emit(AuthEvent.ShowError(result.message))
                }
                is NetworkResult.Loading -> { /* handled by isLoading flag */ }
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
        // Client-side validation
        if (username.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Tüm alanlar doldurulmalıdır")
            return
        }
        if (username.length < 3) {
            _state.value = _state.value.copy(errorMessage = "Kullanıcı adı en az 3 karakter olmalıdır")
            return
        }
        if (password.length < 6) {
            _state.value = _state.value.copy(errorMessage = "Şifre en az 6 karakter olmalıdır")
            return
        }
        if (password != confirmPassword) {
            _state.value = _state.value.copy(errorMessage = "Şifreler eşleşmiyor")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.register(username, password)) {
                is NetworkResult.Success -> {
                    _state.value = AuthState(
                        isAuthenticated = true,
                        username = username
                    )
                    _events.emit(AuthEvent.NavigateToHome)
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    _events.emit(AuthEvent.ShowError(result.message))
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.logout()
            _state.value = AuthState()
            _events.emit(AuthEvent.NavigateToLogin)
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = repository.refreshToken()) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _events.emit(AuthEvent.ShowError("Token başarıyla yenilendi ✓"))
                }
                is NetworkResult.Error -> {
                    _state.value = AuthState()
                    _events.emit(AuthEvent.NavigateToLogin)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}
