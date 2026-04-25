package dev.enes.authapp.domain.repository

import dev.enes.authapp.data.model.AuthResponse
import dev.enes.authapp.domain.model.NetworkResult

/**
 * Auth Repository arayüzü — domain katmanı.
 * Data katmanından bağımsız iş mantığı tanımı.
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): NetworkResult<AuthResponse>
    suspend fun register(username: String, password: String): NetworkResult<AuthResponse>
    suspend fun refreshToken(): NetworkResult<AuthResponse>
    suspend fun logout(): NetworkResult<Unit>
    fun isLoggedIn(): Boolean
    fun getUsername(): String?
}
