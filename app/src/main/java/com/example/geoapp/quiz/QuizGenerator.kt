
// Esta es la implementación del generador de preguntas para el quiz de geografía. Se crean preguntas de capitales, banderas y continentes.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp.quiz

import com.example.geoapp.api.CountryResponse

// Objeto responsable de generar preguntas para el quiz
object QuizGenerator {

    private const val NUM_QUESTIONS_TOTAL = 10
    private const val NUM_OPTIONS = 4

    // Genera una lista de preguntas a partir de una lista de países obtenidos de la API
    fun generateQuestions(countries: List<CountryResponse>): List<Question> {
        val questions = mutableListOf<Question>()

        val validCountries = countries
            .filter { it.name.common.isNotEmpty() && it.continents.isNotEmpty() }
            .shuffled()

        if (validCountries.isEmpty()) return emptyList()

        repeat(NUM_QUESTIONS_TOTAL) {
            // Selecciona aleatoriamente el tipo de pregunta a crear
            when ((0..2).random()) {
                0 -> questions.add(createCapitalQuestion(validCountries.shuffled()))
                1 -> questions.add(createFlagQuestion(validCountries.shuffled()))
                2 -> questions.add(createNotBelongQuestion(validCountries.shuffled()))
            }
        }

        return questions.shuffled()
    }


    // Crea una sola pregunta de "Capital"
    private fun createCapitalQuestion(countries: List<CountryResponse>): Question {
        val selectedCountries = countries.take(NUM_OPTIONS)

        // El primero es la respuesta correcta
        val correctCountry = selectedCountries[0]
        val correctAnswer = correctCountry.capital!![0] // Aseguramos que capital no sea nulo

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

    // Crea una sola pregunta de "Banderas"
    private fun createFlagQuestion(countries: List<CountryResponse>): Question {
        // Aquí asumimos que todos los países tienen una bandera válida
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

    // Crea una sola pregunta de "No Pertenece"
    private fun createNotBelongQuestion(countries: List<CountryResponse>): Question {
        // Filtramos países con continente válido
        val validCountries = countries.filter { it.continents.isNotEmpty() }
        if (validCountries.size < NUM_OPTIONS + 1) {
            // Evita errores si hay pocos datos
            return createCapitalQuestion(countries)
        }

        // Elegimos un continente base (por ejemplo, África)
        val baseCountry = validCountries.random()
        val continent = baseCountry.continents.first()

        // Tomamos 3 países del mismo continente
        val sameContinent = validCountries
            .filter { it.continents.contains(continent) }
            .shuffled()
            .take(NUM_OPTIONS - 1)

        // Y 1 país de un continente diferente
        val different = validCountries
            .filter { !it.continents.contains(continent) }
            .randomOrNull()

        if (different == null || sameContinent.size < 3) {
            return createCapitalQuestion(countries) // fallback
        }

        // Combinamos los países
        val selectedCountries = (sameContinent + different).shuffled()

        return Question(
            text = "¿Qué país no pertenece a $continent?",
            imageUrl = null,
            options = selectedCountries.map { it.name.common },
            correctAnswer = different.name.common,
            category = "CONTINENTES"
        )
    }
}