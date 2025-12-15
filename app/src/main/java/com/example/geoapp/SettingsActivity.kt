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
        // setup logout
        binding.btnSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        // 1. Limpiar SharedPreferences de Login
        val loginPrefs = getSharedPreferences("GeoQuizPrefs", Context.MODE_PRIVATE)
        loginPrefs.edit().clear().apply()

        // 2. Cerrar sesión de Firebase
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        auth.signOut()

        // 3. Cerrar sesión de Google Client y navegar
        val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // 4. Navegar al Login y borrar historial
            val intent = android.content.Intent(this, LoginActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}