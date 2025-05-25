# WallHUB 🎨

WallHUB, Android cihazlar için geliştirilmiş modern ve kullanıcı dostu bir duvar kağıdı uygulamasıdır. Kategorilere ayrılmış yüksek kaliteli duvar kağıtlarını keşfedin, beğenin ve indirin.

## 📱 Özellikler

- **Kategorize Edilmiş Duvar Kağıtları**: Farklı kategorilerde düzenlenmiş zengin duvar kağıdı koleksiyonu
- **HD Kalite**: Yüksek çözünürlüklü duvar kağıtları
- **Kolay İndirme**: Tek tıkla duvar kağıdı indirme
- **Favoriler Sistemi**: Beğendiğiniz duvar kağıtlarını favorilerinize ekleyin
- **Admin Paneli**: Uygulama yönetimi için kapsamlı admin paneli
- **Modern UI/UX**: Kullanıcı dostu ve modern arayüz tasarımı
- **Offline Destek**: İndirilen duvar kağıtlarına çevrimdışı erişim

## 🛠️ Kullanılan Teknolojiler

- **Platform**: Android (Java)
- **IDE**: Android Studio
- **Veritabanı**: Firebase Realtime Database
- **Görsel Yükleme**: Firebase Storage
- **Kütüphaneler**:
  - **Dexter**: Izin yönetimi için
  - **Glide**: Görsel yükleme ve önbellekleme için
  - **Lottie**: Animasyonlar için

## 📸 Ekran Görüntüleri

### Ana Ekran ve Kategoriler
<img src="https://user-images.githubusercontent.com/75747633/134912230-4174607f-5904-4ec4-a868-d9075c83c68c.png" width="250" alt="Ana Ekran"/> <img src="https://user-images.githubusercontent.com/75747633/134912184-cdae0514-b195-4316-9b8b-243986b4d4cf.png" width="250" alt="Kategoriler"/> <img src="https://user-images.githubusercontent.com/75747633/134912187-5d59e05a-ec76-4a27-90be-d8047c28364f.png" width="250" alt="Duvar Kağıtları"/>

### Detay ve İndirme
<img src="https://user-images.githubusercontent.com/75747633/134912194-0008f750-d330-44d7-9423-a0f014f5c78e.png" width="250" alt="Detay Sayfası"/> <img src="https://user-images.githubusercontent.com/75747633/134912208-ac9a5945-1973-4dd2-84bd-80d57241733d.png" width="250" alt="İndirme"/> <img src="https://user-images.githubusercontent.com/75747633/134912245-5d308e43-b3ac-4223-920d-09c32b46f8b0.png" width="250" alt="Giriş Ekranı"/>

### Admin Paneli
<img src="https://user-images.githubusercontent.com/75747633/134912220-7a1b31eb-b76b-42b1-9577-ac3243919c32.png" width="250" alt="Admin Panel 1"/> <img src="https://user-images.githubusercontent.com/75747633/134912223-12387368-7c55-49f9-a9dd-0d1c45adfdcd.png" width="250" alt="Admin Panel 2"/> <img src="https://user-images.githubusercontent.com/75747633/134912226-49875b7b-55be-49b1-8cc4-e783cc0dc38e.png" width="250" alt="Admin Panel 3"/>

## 🚀 Kurulum

### Önkoşullar
- Android Studio (en son sürüm)
- Java 8 veya üzeri
- Firebase projesi
- Android SDK (minimum API 21)

### Adımlar

1. **Projeyi klonlayın**
   ```bash
   git clone https://github.com/EmirHanDinc/WallHUB.git
   ```

2. **Android Studio'da açın**
   - Android Studio'yu açın
   - "Open an existing Android Studio project" seçin
   - Klonladığınız klasörü seçin

3. **Firebase Yapılandırması**
   - [Firebase Console](https://console.firebase.google.com/) adresine gidin
   - Yeni bir proje oluşturun
   - Android uygulaması ekleyin
   - `google-services.json` dosyasını `app/` klasörüne ekleyin

4. **Firebase Özellikleri**
   - Realtime Database'i etkinleştirin
   - Storage'ı etkinleştirin
   - Güvenlik kurallarını yapılandırın

5. **Uygulamayı derleyin ve çalıştırın**
   ```bash
   ./gradlew assembleDebug
   ```

## 📂 Proje Yapısı

```
WallHUB/
├── app/
│   ├── src/main/java/com/emr/wallhub/
│   │   ├── activities/          # Aktivite sınıfları
│   │   ├── adapters/           # RecyclerView adaptörleri
│   │   ├── models/             # Veri modelleri
│   │   ├── utils/              # Yardımcı sınıflar
│   │   └── MainActivity.java   # Ana aktivite
│   ├── src/main/res/
│   │   ├── layout/             # XML layout dosyaları
│   │   ├── drawable/           # Görseller ve drawable'lar
│   │   ├── values/             # Strings, colors, styles
│   │   └── raw/                # Lottie animasyon dosyaları
│   └── build.gradle
├── gradle/
├── build.gradle
└── README.md
```

## 🔥 Firebase Yapılandırması

### Realtime Database Yapısı
```json
{
  "categories": {
    "category_id": {
      "name": "Kategori Adı",
      "image": "kategori_gorseli_url"
    }
  },
  "wallpapers": {
    "wallpaper_id": {
      "title": "Duvar Kağıdı Başlığı",
      "category": "kategori_id",
      "image_url": "yüksek_çözünürlük_url",
      "thumbnail_url": "küçük_görsel_url",
      "downloads": 0,
      "likes": 0
    }
  }
}
```

### Güvenlik Kuralları
```json
{
  "rules": {
    ".read": true,
    ".write": "auth != null"
  }
}
```

## 👨‍💻 Geliştirici

**Emir Han Dinç**
- GitHub: [@EmirHanDinc](https://github.com/EmirHanDinc)

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakınız.

## 🤝 Katkıda Bulunma

1. Bu repository'yi fork edin
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some AmazingFeature'`)
4. Branch'inizi push edin (`git push origin feature/AmazingFeature`)
5. Pull Request oluşturun

## 📞 İletişim

Herhangi bir sorunuz veya öneriniz için lütfen [issue](https://github.com/EmirHanDinc/WallHUB/issues) oluşturun.

## ⭐ Destek

Eğer bu proje işinize yaradıysa, lütfen bir ⭐ vererek destekleyiniz!

---

**WallHUB** - Android için modern duvar kağıdı uygulaması 🎨
