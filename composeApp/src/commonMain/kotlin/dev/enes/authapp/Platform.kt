package dev.enes.authapp

/**
 * Platform-specific bilgiler için expect bildirimleri.
 * Her platform kendi actual implementasyonunu sağlar.
 */
expect fun getPlatformName(): String
