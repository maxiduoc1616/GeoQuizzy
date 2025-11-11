// Esta clase maneja los logros de la aplicación, permitiendo guardar y leer el estado de los logros utilizando SharedPreferences.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.content.Context

object AchievementManager {
    private const val PREFS_NAME = "achievements_prefs"
    private const val KEY_FIRST_QUIZ = "first_quiz"
    private const val KEY_PERFECT_SCORE = "perfect_score"
    private const val KEY_50_QUESTIONS = "fifty_questions"
    const val KEY_TOTAL_QUESTIONS_ANSWERED = "total_questions_answered"

    // Guardar logros
    fun unlockFirstQuiz(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_QUIZ, true).apply()
    }

    fun unlockPerfectScore(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_PERFECT_SCORE, true).apply()
    }

    fun unlock50Questions(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_50_QUESTIONS, true).apply()
    }

    // Leer logros
    fun isFirstQuizUnlocked(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_QUIZ, false)
    }

    fun isPerfectScoreUnlocked(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_PERFECT_SCORE, false)
    }

    fun is50QuestionsUnlocked(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_50_QUESTIONS, false)
    }

    fun getQuestionsAnswered(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TOTAL_QUESTIONS_ANSWERED, 0)
    }
}
