package dev.enes.authapp.data.model

import kotlinx.serialization.Serializable

/**
 * Backend auth endpoint'lerinin yanıt modeli.
 * Mobile client: accessToken + refreshToken + tokenType
 */
@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val tokenType: String
)

/**
 * Backend hata yanıt modeli.
 */
@Serializable
data class ErrorResponse(
    val timestamp: String? = null,
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null
)

/**
 * Login isteği.
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Register isteği.
 */
@Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)

/**
 * Refresh token isteği.
 */
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
