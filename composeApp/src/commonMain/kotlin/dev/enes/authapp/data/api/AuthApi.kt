package dev.enes.authapp.data.api

import dev.enes.authapp.data.model.*
import dev.enes.authapp.util.TokenManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Auth Service API client.
 * Tüm auth endpoint'lerini Ktor ile çağırır.
 * X-Client-Type: mobile header'ı otomatik eklenir.
 */
class AuthApi(
    engine: io.ktor.client.engine.HttpClientEngine,
    private val tokenManager: TokenManager
) {
    companion object {
        // Android emülatörden localhost'a erişim için 10.0.2.2 kullanılır
        private const val BASE_URL = "http://10.0.2.2:8080/api/auth"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    val client = HttpClient(engine) {
        // JSON serialization
        install(ContentNegotiation) {
            json(this@AuthApi.json)
        }

        // Logging
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }

        // Default headers
        defaultRequest {
            header("X-Client-Type", "mobile")
            contentType(ContentType.Application.Json)
        }

        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
        }
    }

    /**
     * Kullanıcı kaydı.
     */
    suspend fun register(username: String, password: String): AuthResponse {
        val response = client.post("$BASE_URL/register") {
            setBody(RegisterRequest(username, password))
        }
        return handleResponse(response)
    }

    /**
     * Kullanıcı girişi.
     */
    suspend fun login(username: String, password: String): AuthResponse {
        val response = client.post("$BASE_URL/login") {
            setBody(LoginRequest(username, password))
        }
        return handleResponse(response)
    }

    /**
     * Token yenileme — refresh token ile yeni access token alır.
     */
    suspend fun refreshToken(refreshToken: String): AuthResponse {
        val response = client.post("$BASE_URL/refresh") {
            setBody(RefreshTokenRequest(refreshToken))
        }
        return handleResponse(response)
    }

    /**
     * Kullanıcı çıkışı — refresh token'ı iptal eder.
     */
    suspend fun logout(refreshToken: String) {
        val response = client.post("$BASE_URL/logout") {
            header("Authorization", "Bearer ${tokenManager.accessToken}")
            setBody(RefreshTokenRequest(refreshToken))
        }
        if (!response.status.isSuccess()) {
            val errorBody = try {
                response.body<ErrorResponse>()
            } catch (_: Exception) {
                null
            }
            throw ApiException(
                message = errorBody?.message ?: "Çıkış yapılırken hata oluştu",
                statusCode = response.status.value
            )
        }
    }

    /**
     * Yanıtı parse eder, hata durumunda ApiException fırlatır.
     */
    private suspend fun handleResponse(response: HttpResponse): AuthResponse {
        if (response.status.isSuccess()) {
            return response.body<AuthResponse>()
        }

        val errorBody = try {
            response.body<ErrorResponse>()
        } catch (_: Exception) {
            null
        }

        throw ApiException(
            message = errorBody?.message ?: "Bilinmeyen bir hata oluştu",
            statusCode = response.status.value
        )
    }
}

/**
 * API hata sınıfı.
 */
class ApiException(
    override val message: String,
    val statusCode: Int
) : Exception(message)
