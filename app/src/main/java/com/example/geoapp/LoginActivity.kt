package com.example.geoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREFS_NAME = "GeoQuizPrefs"
        const val KEY_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Revisa si el usuario ya existe
        checkIfUserIsLoggedIn()

        // Lógica del botón
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun checkIfUserIsLoggedIn() {
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        if (username != null) {
            // Si ya tiene un nombre guardado, saltarse el 'login' y navegar al Home
            navigateToHome()
        }
    }

    private fun handleLogin() {
        val username = binding.etUsername.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un nombre", Toast.LENGTH_SHORT).show()
        } else {
            // Guardar el nombre de usuario
            sharedPreferences.edit().apply {
                putString(KEY_USERNAME, username)
                apply()
            }
            // Navegar al Home
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Cierra LoginActivity para que no pueda volver con el botón "atrás"
    }
}