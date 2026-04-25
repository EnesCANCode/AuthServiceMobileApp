# 📱 Auth Service KMP Mobile App

Bu proje, Spring Boot ile yazılmış bir Backend Auth Servisine bağlanan, **Kotlin Multiplatform (Compose Multiplatform)** kullanılarak geliştirilmiş bir mobil uygulamadır.

Şu anda sadece **Android** hedeflenerek tasarlanmıştır, ancak KMP altyapısı sayesinde kolayca iOS ve diğer platformlara genişletilebilir.

---

## 🎯 Proje Özellikleri

- ✅ **KMP & Compose Multiplatform**: Paylaşılan UI ve Business Logic.
- ✅ **Clean Architecture & MVI**: Sürdürülebilir ve test edilebilir mimari yapısı.
- ✅ **JWT & Refresh Token Yönetimi**: Otomatik token yenileme mekanizması (Token Rotation desteği için hazır).
- ✅ **Dependency Injection (Koin)**: Bağımlılıkların temiz bir şekilde yönetilmesi.
- ✅ **Ktor HTTP Client**: Ağ çağrıları ve API iletişimi.
- ✅ **Multiplatform Settings**: Tokenların (Access & Refresh) güvenli ve kalıcı bir şekilde saklanması.
- ✅ **Premium Tasarım**: Dark Mode öncelikli, Glassmorphism detaylara sahip modern bir arayüz.

---

## 🛠️ Kullanılan Teknolojiler

| Teknoloji | Sürüm | Kullanım Amacı |
| :--- | :--- | :--- |
| **Kotlin** | 2.1.0 | Ana Programlama Dili |
| **Compose Multiplatform** | 1.7.1 | UI Geliştirme (Bildirimsel Arayüz) |
| **Ktor Client** | 3.0.3 | HTTP Ağ İstekleri |
| **Kotlinx Serialization**| 1.7.3 | JSON Ayrıştırma ve Doğrulama |
| **Koin** | 4.0.1 | Dependency Injection (DI) |
| **Multiplatform Settings** | 1.2.0 | Yerel Veri (Token) Saklama |

---

## 📐 Mimari (Clean Architecture + MVI)

Proje, Clean Architecture prensiplerine ve UI katmanında MVI (Model-View-Intent) pattern'ına göre tasarlanmıştır:

1. **UI Layer**: Ekranda görünen her şey (Compose). Durum değişikliklerini (State) ViewModel'den dinler ve eylemleri (Intent) iletir.
2. **ViewModel (MVI)**: İş mantığını yürütür, API'den gelen veriyi `AuthState` ile UI'a iletir.
3. **Domain Layer**: `AuthRepository` interface'ini ve `NetworkResult` nesnesini içerir.
4. **Data Layer**: `AuthRepositoryImpl` ile `AuthApi` (Ktor) entegrasyonu. Ayrıca token saklama işini `TokenManager` üstlenir.

---

## 🚀 Kurulum ve Çalıştırma

### 1. Backend'i Başlatma

Uygulamanın çalışabilmesi için **JWT & Refresh Token Mimarisi** backend'inin (Spring Boot) ayakta olması gerekir.

- Backend projenizin olduğu dizine gidin ve uygulamayı çalıştırın:
  ```bash
  ./mvnw spring-boot:run
  ```
- Backend varsayılan olarak `localhost:8080` portunda çalışmalıdır.

### 2. Uygulamayı Android Emülatöründe Çalıştırma

Mobil uygulama ağ isteklerini `http://10.0.2.2:8080/api/auth` (Android emülatörden localhost'a erişim adresi) adresine yapar. Fiziksel bir cihazda test edecekseniz `AuthApi.kt` içerisindeki `BASE_URL` adresini bilgisayarınızın yerel IP adresi (örn: `192.168.1.x`) ile değiştirmelisiniz.

**Seçenek 1: Android Studio ile**
1. Projeyi Android Studio'da açın.
2. Bir Android Emülatör başlatın.
3. Sağ üstten `composeApp` modülünü seçip **Run (Play)** tuşuna basın.

**Seçenek 2: Terminal ile (APK Oluşturma)**
1. Terminalden proje kök dizinine gidin.
2. Aşağıdaki komut ile Debug APK'sını oluşturun:
   ```bash
   ./gradlew composeApp:assembleDebug
   ```
3. Çıktı APK'sı şurada oluşacaktır: `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
4. APK'yı emülatöre kurmak için:
   ```bash
   adb install composeApp/build/outputs/apk/debug/composeApp-debug.apk
   ```

---

## 🎨 Ekran Görüntüleri ve Tasarım

Uygulama; `Login`, `Register` ve `Home` ekranlarından oluşur.
- **Login/Register:** "Glassmorphism" kart tasarımları, soft gradient renk geçişleri.
- **Home:** Giriş yapan kullanıcının adını, cihaz bilgisini (Android sürümü vb.) gösteren dinamik bir karşılama ekranı.
- Animasyonlu ekran geçişleri ve hata mesajı gösterimleri bulunmaktadır.

---

## 📝 Gelecek Geliştirmeler (TODO)

- [ ] iOS target'ının (`iosArm64`, `iosSimulatorArm64`) aktifleştirilmesi ve Xcode yapılandırması.
- [ ] Uygulama ikonunun (Launcher Icon) eklenmesi.
- [ ] Ktor için otomatik 401 Interceptor'ın detaylandırılarak eklenmesi.
