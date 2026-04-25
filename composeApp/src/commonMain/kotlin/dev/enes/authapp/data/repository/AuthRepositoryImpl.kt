package dev.enes.authapp.data.repository

import dev.enes.authapp.data.api.ApiException
import dev.enes.authapp.data.api.AuthApi
import dev.enes.authapp.data.model.AuthResponse
import dev.enes.authapp.domain.model.NetworkResult
import dev.enes.authapp.domain.repository.AuthRepository
import dev.enes.authapp.util.TokenManager

/**
 * AuthRepository implementasyonu.
 * AuthApi çağrılarını sarmalayıp NetworkResult döner.
 * Token yönetimini otomatik yapar.
 */
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): NetworkResult<AuthResponse> {
        return try {
            val response = authApi.login(username, password)
            // Token'ları kaydet
            response.refreshToken?.let { rt ->
                tokenManager.saveTokens(response.accessToken, rt, username)
            }
            NetworkResult.Success(response)
        } catch (e: ApiException) {
            NetworkResult.Error(e.message, e.statusCode)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Bağlantı hatası")
        }
    }

    override suspend fun register(username: String, password: String): NetworkResult<AuthResponse> {
        return try {
            val response = authApi.register(username, password)
            response.refreshToken?.let { rt ->
                tokenManager.saveTokens(response.accessToken, rt, username)
            }
            NetworkResult.Success(response)
        } catch (e: ApiException) {
            NetworkResult.Error(e.message, e.statusCode)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Bağlantı hatası")
        }
    }

    override suspend fun refreshToken(): NetworkResult<AuthResponse> {
        return try {
            val currentRefreshToken = tokenManager.refreshToken
                ?: return NetworkResult.Error("Refresh token bulunamadı")

            val response = authApi.refreshToken(currentRefreshToken)
            response.refreshToken?.let { rt ->
                tokenManager.saveTokens(response.accessToken, rt)
            }
            NetworkResult.Success(response)
        } catch (e: ApiException) {
            // Refresh başarısız — oturumu kapat
            if (e.statusCode == 403 || e.statusCode == 401) {
                tokenManager.clearTokens()
            }
            NetworkResult.Error(e.message, e.statusCode)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Token yenileme hatası")
        }
    }

    override suspend fun logout(): NetworkResult<Unit> {
        return try {
            val currentRefreshToken = tokenManager.refreshToken
                ?: return NetworkResult.Error("Refresh token bulunamadı")

            authApi.logout(currentRefreshToken)
            tokenManager.clearTokens()
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            // Logout hata verse bile local tokenları temizle
            tokenManager.clearTokens()
            NetworkResult.Success(Unit)
        }
    }

    override fun isLoggedIn(): Boolean = tokenManager.isLoggedIn

    override fun getUsername(): String? = tokenManager.username
}
