package dev.enes.authapp.di

import com.russhwolf.settings.Settings
import dev.enes.authapp.data.api.AuthApi
import dev.enes.authapp.data.repository.AuthRepositoryImpl
import dev.enes.authapp.domain.repository.AuthRepository
import dev.enes.authapp.ui.auth.AuthViewModel
import dev.enes.authapp.util.TokenManager
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin dependency injection modülü.
 */
val appModule = module {
    // Platform
    single { Settings() }
    single { OkHttp.create() }

    // Data
    single { TokenManager(get()) }
    single { AuthApi(get(), get()) }

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // ViewModel
    viewModel { AuthViewModel(get()) }
}

/**
 * Koin'i başlatır. Android Application.onCreate()'de çağrılır.
 */
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
