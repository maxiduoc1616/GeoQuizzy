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

        val validCountries = countries
            .filter { it.name.common.isNotEmpty() && it.continents.isNotEmpty() }
            .shuffled()

        if (validCountries.isEmpty()) return emptyList()

        // 🔹 Mantén el total en 10 preguntas
        repeat(NUM_QUESTIONS_TOTAL) {
            // Elegimos tipo al azar: 0 = capital, 1 = bandera, 2 = no pertenece
            when ((0..2).random()) {
                0 -> questions.add(createCapitalQuestion(validCountries.shuffled()))
                1 -> questions.add(createFlagQuestion(validCountries.shuffled()))
                2 -> questions.add(createNotBelongQuestion(validCountries.shuffled()))
            }
        }

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