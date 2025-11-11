// Esta actividad es la pantalla principal después de iniciar sesión en la aplicación GeoApp.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geoapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bloque para almacenar y mostrar el nombre guardado en SharedPreferences
        val prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
        val username = prefs.getString(LoginActivity.KEY_USERNAME, "Jugador")
        binding.tvWelcome.text = "¡Hola, $username!"

        setupListeners()

        binding.fabSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupListeners() {
        binding.btnStartQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        binding.btnRanking.setOnClickListener {
            startActivity(Intent(this, RankingActivity::class.java))
        }

        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }

        binding.btnShare.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        // Crea un Intent para compartir el enlace de la aplicación (actualmente se comparte solo el repo de GitHub)
        val message = """
            https://github.com/maxiduoc1616/Evaluacion2
            🌍 Descarga la app.
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Compartir usando...")
        startActivity(shareIntent)
    }
}