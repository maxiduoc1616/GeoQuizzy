package com.example.geoapp

import android.content.Context

object AchievementManager {
    private const val PREFS_NAME = "achievements_prefs"
    private const val KEY_FIRST_QUIZ = "first_quiz"
    private const val KEY_PERFECT_SCORE = "perfect_score"
    private const val KEY_50_QUESTIONS = "fifty_questions"

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
}
