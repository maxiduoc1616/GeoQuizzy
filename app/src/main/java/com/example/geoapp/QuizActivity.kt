// Esta actividad gestiona el quiz de preguntas geográficas. Es responsable de cargar las preguntas, mostrar cada pregunta,
// manejar las respuestas del usuario, actualizar el puntaje y navegar a la pantalla de resultados al finalizar el quiz.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.geoapp.api.CountryRepository
import com.example.geoapp.databinding.ActivityQuizBinding
import com.example.geoapp.quiz.Question
import com.example.geoapp.quiz.QuizGenerator
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    // Variables de estado del Quiz
    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0
    private var timer: CountDownTimer? = null

    // Lista de botones
    private lateinit var optionButtons: List<Button>

    companion object {
        private const val QUIZ_DURATION_MS = 30000L // 30 segundos por quiz
        private const val ANSWER_DELAY_MS = 1000L  // 1 segundo de espera
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        optionButtons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)

        setupListeners()
        loadQuizData()
    }

    // Esta función configura los Clicks para los 4 botones de opción

    private fun setupListeners() {
        optionButtons.forEach { button ->
            button.setOnClickListener {
                checkAnswer(button.text.toString())
            }
        }
    }

     // Esta función carga los datos de la API usando Coroutines y el repositorio

    private fun loadQuizData() {
        showLoading(true) // Muestra la barrita de progreso

        lifecycleScope.launch {
            // Llama al Repositorio (Patrón del Profesor)
            val result = CountryRepository.fetchCountries()

            result.onSuccess { countries ->
                questions = QuizGenerator.generateQuestions(countries)

                if (questions.isEmpty()) {
                    showError("No se pudieron generar preguntas.")
                } else {
                    // Inicia el quiz
                    showLoading(false)
                    startQuiz()
                }
            }

            result.onFailure { error ->
                // En caso de que haya un error de Red
                Log.e("QuizActivity", "Error al cargar países", error)
                showError("Error de red. Intenta de nuevo.")
            }
        }
    }


    // Esta función inicia el quiz
    private fun startQuiz() {
        currentQuestionIndex = 0
        score = 0
        displayCurrentQuestion()
        startTimer()
    }


    // Esta función muestra la pregunta actual en la UI

    private fun displayCurrentQuestion() {
        if (currentQuestionIndex >= questions.size) return

        resetButtonColors()
        enableButtons(true)

        val question = questions[currentQuestionIndex]

        // Actualiza textos
        binding.tvQuestion.text = question.text
        binding.tvQuestionCounter.text = "${currentQuestionIndex + 1}/${questions.size}"

        // Asigna la categoría al TextView del tag
        binding.tvCategoryTag.text = question.category

        // Asigna texto a los botones
        optionButtons.forEachIndexed { index, button ->
            button.text = question.options[index]
        }

        // Maneja la imagen de la bandera
        if (question.imageUrl != null) {
            binding.ivFlag.visibility = View.VISIBLE
            Glide.with(this)
                .load(question.imageUrl)
                .into(binding.ivFlag)
        } else {
            binding.ivFlag.visibility = View.GONE
        }
    }

    // Esta función inicia el temporizador del quiz

    private fun startTimer() {
        timer?.cancel() // Cancela cualquier timer que haya quedado de antes
        timer = object : CountDownTimer(QUIZ_DURATION_MS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Formatea el tiempo a "mm:ss"
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                binding.tvTimer.text = String.format("%02d:%02d", 0, seconds)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                Toast.makeText(this@QuizActivity, "¡Tiempo agotado!", Toast.LENGTH_SHORT).show()
                endQuiz()
            }
        }.start()
    }

    // Esta función comprueba la respuesta seleccionada
    private fun checkAnswer(selectedAnswer: String) {
        enableButtons(false) // Deshabilita botones
        timer?.cancel() // Pausa el timer al responder

        val correct = questions[currentQuestionIndex].correctAnswer

        if (selectedAnswer == correct) {
            // Respuesta correcta
            score++
            highlightButton(selectedAnswer, true)
        } else {
            // Respuesta incorrecta
            highlightButton(selectedAnswer, false) // Pinta la opción incorrecta de rojo
            highlightButton(correct, true) // Pinta la opción correcta de verde
        }

        // Espera 1 segundo antes de pasar a la siguiente pregunta
        Handler(Looper.getMainLooper()).postDelayed({
            nextQuestion()
        }, ANSWER_DELAY_MS)
    }


    // Esta función carga la siguiente pregunta o termina el quiz.

    private fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayCurrentQuestion()
            startTimer() // Reinicia el timer para la nueva pregunta
        } else {
            // Se acabaron las preguntas
            endQuiz()
        }
    }


    // Esta función termina el juego y navega a la pantalla de resultados.
    private fun endQuiz() {
        timer?.cancel() // Cancela el timer definitivamente

        // Desbloquear logros
        AchievementManager.unlockFirstQuiz(this) // Siempre que completes al menos un quiz

        if (score == questions.size) {
            AchievementManager.unlockPerfectScore(this)
        }

        val prefs = getSharedPreferences("achievements_prefs", Context.MODE_PRIVATE)
        val totalQuestions = prefs.getInt(AchievementManager.KEY_TOTAL_QUESTIONS_ANSWERED, 0) + questions.size
        prefs.edit().putInt(AchievementManager.KEY_TOTAL_QUESTIONS_ANSWERED, totalQuestions).apply()

        if (totalQuestions >= 50) {
            AchievementManager.unlock50Questions(this)
        }

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("FINAL_SCORE", score)
        intent.putExtra("TOTAL_QUESTIONS", questions.size)
        startActivity(intent)
        finish()
    }


    // Funciones de ayuda

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.quizGroup.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        showLoading(false)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun enableButtons(isEnabled: Boolean) {
        optionButtons.forEach { it.isEnabled = isEnabled }
    }

    private fun resetButtonColors() {
        optionButtons.forEach {
            it.backgroundTintList = ContextCompat.getColorStateList(this, R.color.button_green_color)
        }
    }

    private fun highlightButton(answerText: String, isCorrect: Boolean) {
        val button = optionButtons.find { it.text == answerText }
        val color = if (isCorrect) Color.parseColor("#68B144") else Color.RED
        button?.backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}