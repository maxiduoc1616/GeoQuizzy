package com.example.geoapp.db // Asegúrate que este sea tu paquete

// Data class para representar un objeto Score leído de la BD
data class Score(
    val id: Long,
    val username: String,
    val score: Int,
    val date: Long
)

// Data class para representar un objeto Achievement leído de la BD
data class Achievement(
    val id: Long,
    val name: String,
    val description: String,
    val isUnlocked: Boolean
)