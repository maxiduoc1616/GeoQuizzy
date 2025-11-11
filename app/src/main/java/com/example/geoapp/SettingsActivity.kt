// Esta actividad permite al usuario ajustar configuraciones como el modo oscuro y cambiar su nombre de usuario.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.geoapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    companion object {
        const val PREFS_SETTINGS_NAME = "SettingsPrefs"
        const val KEY_DARK_MODE = "darkMode"
        const val KEY_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el botón de "atrás" en la barra de herramientas
        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        val sharedPreferences = getSharedPreferences(PREFS_SETTINGS_NAME, Context.MODE_PRIVATE)

        // Dark Mode 
        val isDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false)
        binding.switchDarkMode.isChecked = isDarkMode

        // Maneja el cambio de estado del switch
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Cargar el nombre de usuario actual
        val currentUsername = sharedPreferences.getString(KEY_USERNAME, "")
        binding.etUsername.setText(currentUsername)

        // Maneja el clic en el botón de guardar nombre de usuario
        binding.btnSaveUsername.setOnClickListener {
            val newUsername = binding.etUsername.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                sharedPreferences.edit().putString(KEY_USERNAME, newUsername).apply()
                Toast.makeText(this, "Nombre guardado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}