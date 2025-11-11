// En este archivo se definen los modelos de datos para la aplicación GeoApp, incluyendo puntuaciones y logros.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.db

import androidx.annotation.DrawableRes

// Modelo de datos para una puntuación en el quiz
data class Score(
    val id: Long,
    val username: String,
    val score: Int,
    val date: Long
)
// Modelo de datos para un logro en el quiz
data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    val iconResId: Int,
    val isUnlocked: Boolean = false, 
    val progress: Int = 0,
    val maxProgress: Int = 100
)
