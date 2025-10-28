package com.example.geoapp.quiz

import com.example.geoapp.api.CountryResponse

/**
 * Objeto Singleton que genera una lista de preguntas
 * a partir de la respuesta de la API de países.
 */
object QuizGenerator {

    private const val NUM_QUESTIONS_TOTAL = 10
    private const val NUM_OPTIONS = 4

    /**
     * Función principal. Crea una lista de 10 preguntas (5 de capitales, 5 de banderas).
     */
    fun generateQuestions(countries: List<CountryResponse>): List<Question> {
        val questions = mutableListOf<Question>()

        // 1. Filtra la lista de países
        // Nos aseguramos de que tengan capital y un nombre común
        val validCountries = countries
            .filter { it.capital != null && it.capital.isNotEmpty() && it.name.common.isNotEmpty() }
            .shuffled() // Barajamos la lista de países

        if (validCountries.isEmpty()) {
            return emptyList() // Devuelve lista vacía si la API no trajo datos válidos
        }

        // 2. Crear 5 preguntas de Capitales
        for (i in 0 until (NUM_QUESTIONS_TOTAL / 2)) {
            val question = createCapitalQuestion(validCountries.shuffled())
            questions.add(question)
        }

        // 3. Crear 5 preguntas de Banderas
        for (i in 0 until (NUM_QUESTIONS_TOTAL / 2)) {
            val question = createFlagQuestion(validCountries.shuffled())
            questions.add(question)
        }

        // 4. Barajamos el orden final de las preguntas
        return questions.shuffled()
    }

    /**
     * Crea una sola pregunta de "Capital".
     */
    private fun createCapitalQuestion(countries: List<CountryResponse>): Question {
        // Obtenemos 4 países al azar de la lista ya barajada
        val selectedCountries = countries.take(NUM_OPTIONS)

        // El primero de la lista será la respuesta correcta
        val correctCountry = selectedCountries[0]
        val correctAnswer = correctCountry.capital!![0] // Sabemos que no es nulo por el filtro

        // Las opciones son las capitales de los 4 países
        val options = selectedCountries.map { it.capital!![0] }.shuffled() // Barajamos las opciones

        return Question(
            text = "¿Cuál es la capital de ${correctCountry.name.common}?",
            imageUrl = null, // No hay imagen para esta pregunta
            options = options,
            correctAnswer = correctAnswer,
            category = "CAPITALES"
        )
    }

    /**
     * Crea una sola pregunta de "Bandera".
     */
    private fun createFlagQuestion(countries: List<CountryResponse>): Question {
        // Obtenemos 4 países al azar
        // Usamos .drop(NUM_OPTIONS) para intentar no repetir los de las capitales
        val selectedCountries = countries.drop(NUM_OPTIONS).take(NUM_OPTIONS)

        // El primero es la respuesta correcta
        val correctCountry = selectedCountries[0]
        val correctAnswer = correctCountry.name.common

        // Las opciones son los nombres de los 4 países
        val options = selectedCountries.map { it.name.common }.shuffled() // Barajamos las opciones

        return Question(
            text = "¿A qué país pertenece esta bandera?",
            imageUrl = correctCountry.flags.png, // La URL de la imagen de la bandera
            options = options,
            correctAnswer = correctAnswer,
            category = "BANDERAS"
        )
    }
}