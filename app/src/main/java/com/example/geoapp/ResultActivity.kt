package com.example.geoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.geoapp.databinding.ActivityResultBinding
import com.example.geoapp.db.QuizRepository
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    // Constantes para los IDs de los logros
    companion object {
        private const val ACHIEVEMENT_ID_FIRST_QUIZ = 1L
        private const val ACHIEVEMENT_ID_PERFECT_SCORE = 2L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Recibir datos del Intent
        val score = intent.getIntExtra("FINAL_SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 10)

        // 2. Mostrar el puntaje
        binding.tvScore.text = "$score / $totalQuestions"
        binding.tvMotivationalMessage.text = getMotivationalMessage(score, totalQuestions)

        // 3. Guardar datos en la BD (usando Coroutines)
        saveResults(score, totalQuestions)

        // 4. Configurar botones
        setupListeners()
    }

    /**
     * Guarda el puntaje y revisa los logros usando el Repositorio.
     * ¡Esta es la lógica clave de la Fase 5!
     */
    private fun saveResults(score: Int, totalQuestions: Int) {
        // Obtener el nombre de usuario de SharedPreferences
        val prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
        val username = prefs.getString(LoginActivity.KEY_USERNAME, "Jugador") ?: "Jugador"

        // Usar lifecycleScope para la Coroutine (como en el proyecto de referencia)
        lifecycleScope.launch {

            // --- 1. Guardar el puntaje ---
            val scoreResult = QuizRepository.insertScore(this@ResultActivity, username, score)

            scoreResult.onSuccess { rowId ->
                Log.d("ResultActivity", "Puntaje guardado con ID: $rowId")
            }.onFailure { e ->
                Log.e("ResultActivity", "Error al guardar puntaje", e)
                Toast.makeText(this@ResultActivity, "Error al guardar puntaje", Toast.LENGTH_SHORT).show()
            }

            // --- 2. Desbloquear Logro: "Primeros Pasos" (ID 1) ---
            // Siempre se desbloquea al terminar un quiz
            val firstQuizResult = QuizRepository.unlockAchievement(this@ResultActivity, ACHIEVEMENT_ID_FIRST_QUIZ)

            firstQuizResult.onSuccess { rowsAffected ->
                if (rowsAffected > 0) {
                    Log.d("ResultActivity", "Logro 1 'Primeros Pasos' desbloqueado")
                    // (Opcional) Mostrar un Toast de "Logro desbloqueado"
                }
            }.onFailure { e ->
                Log.e("ResultActivity", "Error al desbloquear logro 1", e)
            }

            // --- 3. Desbloquear Logro: "Cerebrito" (ID 2) ---
            // Solo si el puntaje es perfecto
            if (score == totalQuestions && totalQuestions > 0) {
                val perfectScoreResult = QuizRepository.unlockAchievement(this@ResultActivity, ACHIEVEMENT_ID_PERFECT_SCORE)

                perfectScoreResult.onSuccess { rowsAffected ->
                    if (rowsAffected > 0) {
                        Log.d("ResultActivity", "Logro 2 'Cerebrito' desbloqueado")
                        Toast.makeText(this@ResultActivity, "¡Logro: Cerebrito desbloqueado!", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure { e ->
                    Log.e("ResultActivity", "Error al desbloquear logro 2", e)
                }
            }
        }
    }

    /**
     * Devuelve un mensaje motivador basado en el puntaje.
     */
    private fun getMotivationalMessage(score: Int, total: Int): String {
        return when {
            score == total -> "¡Perfecto! ¡Eres un genio!"
            score > total * 0.7 -> "¡Excelente trabajo!"
            score > total * 0.4 -> "¡Nada mal! Sigue practicando."
            else -> "¡Buen intento! La próxima será mejor."
        }
    }

    /**
     * Configura los listeners para los botones de navegación.
     */
    private fun setupListeners() {
        binding.btnRestartQuiz.setOnClickListener {
            // Vuelve a iniciar el quiz
            startActivity(Intent(this, QuizActivity::class.java))
            finish()
        }

        binding.btnBackToHome.setOnClickListener {
            // Vuelve al menú principal
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    // Evita que el usuario pueda volver a la pantalla de Quiz con el botón "atrás"
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}