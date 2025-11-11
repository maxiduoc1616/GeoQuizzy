// Esta actividad muestra una lista de logros obtenidos por el usuario en la aplicación GeoApp.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoapp.databinding.ActivityAchievementsBinding
import com.example.geoapp.db.Achievement

class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la Toolbar como la ActionBar de la actividad.
        setSupportActionBar(binding.toolbarAchievements)
        // Muestra el botón de flecha para "volver atrás".
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val achievements = listOf(
            Achievement(
                id = 1,
                name = "Primeros Pasos",
                description = "Completa tu primer quiz",
                iconResId = R.drawable.ic_achievement_first_quiz,
                isUnlocked = AchievementManager.isFirstQuizUnlocked(this)
            ),
            Achievement(
                id = 2,
                name = "Cerebrito",
                description = "Consigue un 100% de aciertos",
                iconResId = R.drawable.ic_achievement_perfect_score,
                isUnlocked = AchievementManager.isPerfectScoreUnlocked(this)
            ),
            Achievement(
                id = 3,
                name = "Trotamundos",
                description = "Responde 50 preguntas",
                iconResId = R.drawable.ic_achievement_50_questions,
                progress = AchievementManager.getQuestionsAnswered(this),
                maxProgress = 50
            )
        )


        val adapter = AchievementAdapter(achievements)
        binding.rvAchievements.layoutManager = LinearLayoutManager(this)
        binding.rvAchievements.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        // Cierra la actividad actual y regresa a la pantalla anterior.
        finish()
        return true
    }
}