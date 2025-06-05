package com.example.hey_mom.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hey_mom.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherFragment : Fragment() {

    private lateinit var tvLocation: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvWeatherDescription: TextView
    private lateinit var tvFeelsLike: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvVisibility: TextView
    private lateinit var tvUvIndex: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var fabRefresh: FloatingActionButton

    // Baby care recommendation views
    private lateinit var tvTemperatureAdvice: TextView
    private lateinit var tvComfortAdvice: TextView
    private lateinit var tvUvAdvice: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val apiKey = "74d27535f8404707a51184342250106"
    private val locationPermissionRequestCode = 1001

    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_weather, container, false)
        initializeViews(view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupClickListeners()

        if (checkLocationPermission()) {
            getCurrentLocationAndWeather()
        } else {
            requestLocationPermission()
        }

        return view
    }

    private fun initializeViews(view: View) {
        tvLocation = view.findViewById(R.id.tvLocation)
        tvTemperature = view.findViewById(R.id.tvTemperature)
        tvWeatherDescription = view.findViewById(R.id.tvWeatherDescription)
        tvFeelsLike = view.findViewById(R.id.tvFeelsLike)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvWindSpeed = view.findViewById(R.id.tvWindSpeed)
        tvVisibility = view.findViewById(R.id.tvVisibility)
        tvUvIndex = view.findViewById(R.id.tvUvIndex)
        ivWeatherIcon = view.findViewById(R.id.ivWeatherIcon)
        progressBar = view.findViewById(R.id.progressBar)
        fabRefresh = view.findViewById(R.id.fabRefresh)

        // Initialize baby care views
        tvTemperatureAdvice = view.findViewById(R.id.tvTemperatureAdvice)
        tvComfortAdvice = view.findViewById(R.id.tvComfortAdvice)
        tvUvAdvice = view.findViewById(R.id.tvUvAdvice)
    }

    private fun setupClickListeners() {
        fabRefresh.setOnClickListener {
            if (checkLocationPermission()) {
                getCurrentLocationAndWeather()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            locationPermissionRequestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndWeather()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
                currentLat = 26.1445
                currentLon = 91.7362

                getWeatherData()
            }
        }
    }

    private fun getCurrentLocationAndWeather() {
        if (checkLocationPermission()) {
            showLoading(true)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLat = location.latitude
                        currentLon = location.longitude
                        getWeatherData()
                    } else {
                        currentLat = 26.1445
                        currentLon = 91.7362

                        tvLocation.text = "Guwahati, Assam"
                        getWeatherData()
                    }
                }
                .addOnFailureListener {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                    currentLat = 26.1445
                    currentLon = 91.7362

                    tvLocation.text = "Guwahati, Assam"
                    getWeatherData()
                }
        }
    }

    private fun getWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherUrl =
                    "https://api.weatherapi.com/v1/current.json?key=$apiKey&q=$currentLat,$currentLon&aqi=yes"
                val response = fetchWeatherData(weatherUrl)

                withContext(Dispatchers.Main) {
                    if (response != null) {
                        parseAndDisplayWeather(response)
                    } else {
                        showError("Failed to fetch weather data")
                    }
                    showLoading(false)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                    showLoading(false)
                }
            }
        }
    }

    private fun fetchWeatherData(urlString: String): String? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    reader.readText()
                }
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseAndDisplayWeather(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val location = jsonObject.getJSONObject("location")
            val current = jsonObject.getJSONObject("current")
            val condition = current.getJSONObject("condition")

            val locationName = "${location.getString("name")}, ${location.getString("region")}"
            val temperature = current.getDouble("temp_c").toInt()
            val feelsLike = current.getDouble("feelslike_c").toInt()
            val humidity = current.getInt("humidity")
            val windSpeed = current.getDouble("wind_kph")
            val visibility = current.getDouble("vis_km")
            val uvIndex = current.getDouble("uv")
            val weatherDescription = condition.getString("text")
            val iconCode = condition.getString("icon")

            tvLocation.text = locationName
            tvTemperature.text = "${temperature}°C"
            tvFeelsLike.text = "Feels like ${feelsLike}°C"
            tvWeatherDescription.text = weatherDescription
            tvHumidity.text = "${humidity}%"
            tvWindSpeed.text = "${windSpeed.toInt()} km/h"
            tvVisibility.text = "${visibility.toInt()} km"
            tvUvIndex.text = uvIndex.toString()

            setWeatherIcon(weatherDescription.lowercase(), iconCode)

            // Update baby care recommendations
            updateBabyCareRecommendations(temperature, feelsLike, humidity, uvIndex, weatherDescription)

        } catch (e: Exception) {
            showError("Error parsing weather data: ${e.message}")
        }
    }

    private fun updateBabyCareRecommendations(
        temperature: Int,
        feelsLike: Int,
        humidity: Int,
        uvIndex: Double,
        weatherDescription: String
    ) {
        // Temperature-based recommendations
        val tempAdvice = when {
            feelsLike <= 16 -> "🧥 Baby needs warm clothing and blanket. Keep indoors if possible."
            feelsLike in 17..20 -> "👕 Light layers recommended. A light blanket may be needed."
            feelsLike in 21..26 -> "👶 Comfortable temperature. Light clothing is perfect."
            feelsLike in 27..30 -> "🌡️ Keep baby cool. Light, breathable fabrics recommended."
            else -> "❄️ Very hot! Keep baby in air-conditioned space. Plenty of fluids needed."
        }

        // Humidity and comfort recommendations
        val comfortAdvice = when {
            humidity < 30 -> "💨 Low humidity. Use humidifier and keep baby hydrated."
            humidity in 30..60 -> "✅ Comfortable humidity levels. Baby should feel good."
            humidity > 60 -> "💧 High humidity. Ensure good ventilation and lighter clothing."
            else -> "Monitoring comfort levels..."
        }

        // UV protection recommendations
        val uvAdvice = when {
            uvIndex <= 2 -> "☁️ Low UV. Safe for short outdoor activities."
            uvIndex in 3.0..5.0 -> "🧴 Moderate UV. Use baby sunscreen if going outside."
            uvIndex in 6.0..7.0 -> "🏠 High UV. Limit outdoor exposure. Shade essential."
            uvIndex in 8.0..10.0 -> "⚠️ Very high UV. Avoid direct sunlight. Stay indoors."
            else -> "🚨 Extreme UV! Keep baby indoors. No direct sun exposure."
        }

        // Additional weather-specific advice
        val weatherSpecificAdvice = when {
            weatherDescription.contains("rain", ignoreCase = true) ->
                " Rainy weather - keep baby dry and warm."
            weatherDescription.contains("snow", ignoreCase = true) ->
                " Snowy conditions - extra warm clothing needed."
            weatherDescription.contains("wind", ignoreCase = true) ->
                " Windy weather - protect from drafts."
            else -> ""
        }

        tvTemperatureAdvice.text = tempAdvice + weatherSpecificAdvice
        tvComfortAdvice.text = comfortAdvice
        tvUvAdvice.text = uvAdvice
    }

    private fun setWeatherIcon(condition: String, iconCode: String) {
        val iconResource = when {
            condition.contains("sunny") || condition.contains("clear") -> R.drawable.ic_weather_sunny
            condition.contains("cloudy") || condition.contains("overcast") -> R.drawable.ic_weather_cloudy
            condition.contains("rain") || condition.contains("drizzle") -> R.drawable.ic_weather_rainy
            condition.contains("snow") -> R.drawable.ic_weather_snowy
            condition.contains("thunder") || condition.contains("storm") -> R.drawable.ic_weather_stormy
            condition.contains("fog") || condition.contains("mist") -> R.drawable.ic_weather_foggy
            condition.contains("partly") -> R.drawable.ic_weather_partly_cloudy
            else -> R.drawable.ic_weather_sunny
        }

        ivWeatherIcon.setImageResource(iconResource)
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        fabRefresh.isEnabled = !show
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        tvLocation.text = "Weather Unavailable"
        tvTemperature.text = "--°C"
        tvFeelsLike.text = "Feels like --°C"
        tvWeatherDescription.text = "Unable to load weather"
        tvHumidity.text = "--%"
        tvWindSpeed.text = "-- km/h"
        tvVisibility.text = "-- km"
        tvUvIndex.text = "--"

        // Reset baby care recommendations
        tvTemperatureAdvice.text = "Unable to provide recommendations"
        tvComfortAdvice.text = "Weather data unavailable"
        tvUvAdvice.text = "Cannot assess UV levels"
    }
}