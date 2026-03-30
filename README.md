
Hey MOM! is an intuitive mobile app built for new and experienced parents alike. It centralizes essential baby care routines — from feeding and sleep tracking to vaccination reminders — all in one place. With smart integrations like Google Maps and real-time weather suggestions, Hey MOM! goes beyond a simple tracker to become a complete care companion.

---

## ✨ Features

### 🍼 Feeding Schedule Tracker
- Log and monitor breast or bottle feeding sessions
- Track feeding duration, quantity, and intervals
- Receive reminders for upcoming feeding times
- View feeding history and patterns over time

### 😴 Sleep Time Monitor
- Record sleep and wake cycles throughout the day
- Visualize sleep patterns to identify trends
- Set gentle reminders for nap schedules
- Track total daily sleep duration

### 💉 Vaccination Reminders
- Built-in vaccination schedule based on standard pediatric guidelines
- Timely push notifications before upcoming vaccine dates
- Log completed vaccinations with date and notes
- Never miss an important immunization milestone

### 📍 Nearby Clinic Locator (Google Maps API)
- Locate child clinics, pediatricians, and hospitals nearby
- Interactive map view with markers for healthcare facilities
- Get directions directly within the app
- View clinic details such as contact info and operating hours

### 🌤️ Weather-Based Baby Care Suggestions
- Real-time weather data integrated into the app
- Contextual tips for baby comfort based on current conditions (e.g., hydration reminders on hot days, layering advice on cold days)
- Alerts for weather conditions that may affect outdoor activities

### 🎨 Simple & Intuitive UI
- Clean, parent-friendly interface designed for quick access
- Minimal learning curve — usable even during sleepless nights
- Accessible design with readable fonts and clear visual hierarchy
- Soft color palette to reduce eye strain

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Platform | Android (Java / Kotlin) |
| Maps & Location | Google Maps API |
| Weather Data | OpenWeatherMap API (or similar) |
| Local Storage | SQLite / Room Database |
| Notifications | Android AlarmManager / WorkManager |
| UI Components | Android Material Design |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable version)
- Android SDK 21 (Lollipop) or higher
- A Google Maps API key
- A Weather API key (e.g., OpenWeatherMap)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/babycare-app.git
   cd heymom-app
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select **Open an existing project**
   - Navigate to the cloned directory

3. **Configure API Keys**

   Add your API keys to `local.properties` or `strings.xml`:
   ```xml
   <string name="google_maps_key">YOUR_GOOGLE_MAPS_API_KEY</string>
   <string name="weather_api_key">YOUR_WEATHER_API_KEY</string>
   ```

4. **Build and Run**
   - Connect a physical device or start an emulator
   - Click **Run ▶** in Android Studio

---




## 🔐 Permissions Required

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.VIBRATE" />
```

---

## 🤝 Contributing

Contributions are welcome! If you'd like to improve Hey MOM!:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature-name`)
5. Open a Pull Request

---



---

> 💙 *Built with love for every parent navigating the beautiful journey of parenthood.*
