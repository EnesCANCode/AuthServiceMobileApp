package dev.enes.authapp.util

import com.russhwolf.settings.Settings

/**
 * Token yönetimi — multiplatform-settings ile.
 * Access ve refresh tokenları güvenli şekilde saklar.
 */
class TokenManager(private val settings: Settings) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USERNAME = "username"
    }

    var accessToken: String?
        get() = settings.getStringOrNull(KEY_ACCESS_TOKEN)
        set(value) {
            if (value != null) settings.putString(KEY_ACCESS_TOKEN, value)
            else settings.remove(KEY_ACCESS_TOKEN)
        }

    var refreshToken: String?
        get() = settings.getStringOrNull(KEY_REFRESH_TOKEN)
        set(value) {
            if (value != null) settings.putString(KEY_REFRESH_TOKEN, value)
            else settings.remove(KEY_REFRESH_TOKEN)
        }

    var username: String?
        get() = settings.getStringOrNull(KEY_USERNAME)
        set(value) {
            if (value != null) settings.putString(KEY_USERNAME, value)
            else settings.remove(KEY_USERNAME)
        }

    val isLoggedIn: Boolean
        get() = accessToken != null && refreshToken != null

    fun saveTokens(accessToken: String, refreshToken: String, username: String? = null) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        if (username != null) this.username = username
    }

    fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_USERNAME)
    }
}
