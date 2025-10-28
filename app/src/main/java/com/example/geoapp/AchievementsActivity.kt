package com.example.geoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoapp.databinding.ActivityAchievementsBinding

data class Achievement(
    val id: Int,
    val title: String,
    val description: String,
    var completed: Boolean
)

class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding
    private lateinit var achievements: List<Achievement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lista de logros
        achievements = listOf(
            Achievement(1, "Primeros Pasos", "Completa tu primer quiz", AchievementManager.isFirstQuizUnlocked(this)),
            Achievement(2, "Cerebrito", "Consigue un 100% de aciertos (10/10)", AchievementManager.isPerfectScoreUnlocked(this)),
            Achievement(3, "Trotamundos", "Responde 50 preguntas correctamente en total", AchievementManager.is50QuestionsUnlocked(this))
        )


        // Mostrar logros
        showAchievements()
    }

    private fun showAchievements() {
        binding.achievementsContainer.removeAllViews()

        // Botones de logros
        for (achievement in achievements) {
            val button = Button(this)
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }

            button.text = "${achievement.title} ${if (achievement.completed) "✅" else "❌"}"
            button.setOnClickListener {
                Toast.makeText(this, achievement.description, Toast.LENGTH_SHORT).show()
            }

            binding.achievementsContainer.addView(button)
        }

        // Botón para regresar al HomeActivity
        val homeButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 16, 0, 16) }

            text = "Regresar al inicio"
            setOnClickListener {
                startActivity(Intent(this@AchievementsActivity, HomeActivity::class.java))
                finish()
            }
        }

        binding.achievementsContainer.addView(homeButton)
    }
}
