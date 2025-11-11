// En este archivo se define la clase de datos Question para representar preguntas en el quiz de la aplicación GeoApp.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.quiz

// Clase de datos que representa una pregunta del quiz
data class Question(
    val text: String,                   // El texto de la pregunta (ej. "¿Capital de...?")
    val imageUrl: String?,              // URL de la imagen (para preguntas de banderas)
    val options: List<String>,          // Lista de 4 opciones de respuesta
    val correctAnswer: String,          // El texto de la respuesta correcta
    val category: String                // La categoría o tag a la que pertenece la pregunta
)