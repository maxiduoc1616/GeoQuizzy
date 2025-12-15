
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

        // Pre-filtrado básico
        val validCountries = countries
            .filter { it.name.common.isNotEmpty() && it.continents.isNotEmpty() }
            
        if (validCountries.isEmpty() || validCountries.size < NUM_OPTIONS) return emptyList()

        repeat(NUM_QUESTIONS_TOTAL) {
            // Selecciona aleatoriamente, pero se asegura de que el tipo de pregunta sea viable
            val questionType = (0..5).random()
            val availableQuestion = when (questionType) {
                0 -> createCapitalQuestion(validCountries.shuffled())
                1 -> createFlagQuestion(validCountries.shuffled()) // Pasamos lista barajada
                2 -> createNotBelongQuestion(validCountries.shuffled())
                3 -> createLargerAreaQuestion(validCountries.shuffled())
                4 -> createLargerPopulationQuestion(validCountries.shuffled())
                5 -> createReverseCapitalQuestion(validCountries.shuffled())
                else -> null
            }
            
            if (availableQuestion != null) {
                questions.add(availableQuestion)
            } else {
                 // Fallback: tratar de generar una de bandera que es la más segura si hay >= 4 países
                 // O simplemente saltar esta iteración (el quiz tendrá menos preguntas, o hacer un while)
                 try {
                     questions.add(createFlagQuestion(validCountries.shuffled()))
                 } catch (e: Exception) {
                     // Ya no hay nada que hacer
                 }
            }
        }

        return questions.shuffled()
    }


    // Crea una sola pregunta de "Capital"
    private fun createCapitalQuestion(countries: List<CountryResponse>): Question? {
        // Filtramos solo países que tengan capital
        val countriesWithCapital = countries.filter { !it.capital.isNullOrEmpty() }
        
        if (countriesWithCapital.size < NUM_OPTIONS) return null

        val selectedCountries = countriesWithCapital.take(NUM_OPTIONS)

        // El primero es la respuesta correcta
        val correctCountry = selectedCountries[0]
        val correctAnswer = correctCountry.capital?.get(0) ?: return null

        // Las opciones son las capitales de los 4 países
        val options = selectedCountries.mapNotNull { it.capital?.get(0) }.shuffled()
        
        if (options.size < NUM_OPTIONS) return null // Por seguridad

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
        // Simplemente tomamos 4 países. Asumimos que la lista ya viene barajada y tiene tamaño >= 4
        // CORRECCIÓN: No hacer drop, ya que si la lista es pequeña (ej. 4 items en modo estudio), fallará.
        val selectedCountries = countries.take(NUM_OPTIONS)

        // El primero es la respuesta correcta
        val correctCountry = selectedCountries[0]
        val correctAnswer = correctCountry.name.common

        // Las opciones son los nombres de los 4 países
        val options = selectedCountries.map { it.name.common }.shuffled() 

        return Question(
            text = "¿A qué país pertenece esta bandera?",
            imageUrl = correctCountry.flags.png,
            options = options,
            correctAnswer = correctAnswer,
            category = "BANDERAS"
        )
    }

    // Crea una sola pregunta de "No Pertenece"
    private fun createNotBelongQuestion(countries: List<CountryResponse>): Question? {
        // Filtramos países con continente válido
        val validCountries = countries.filter { it.continents.isNotEmpty() }
        if (validCountries.size < NUM_OPTIONS + 1) {
             return null
        }

        // Intentos para encontrar una combinación válida
        // Como es aleatorio, podría fallar al encontrar 3 del mismo continente si todos son distintos
        // Hacemos un intento simple y si falla retornamos null (se manejará en el bucle principal)
        
        val baseCountry = validCountries.random()
        val continent = baseCountry.continents.first()

        val sameContinent = validCountries
            .filter { it.continents.contains(continent) }
            .shuffled()
            .take(NUM_OPTIONS - 1) // 3 países

        if (sameContinent.size < 3) return null

        val different = validCountries
            .filter { !it.continents.contains(continent) }
            .randomOrNull() ?: return null // Si no hay ninguno de otro continente

        // Combinamos
        val selectedCountries = (sameContinent + different).shuffled()

        return Question(
            text = "¿Qué país no pertenece a $continent?",
            imageUrl = null,
            options = selectedCountries.map { it.name.common },
            correctAnswer = different.name.common,
            category = "CONTINENTES"
        )
    }

    // Crea una pregunta de "¿Qué país es más grande?"
    private fun createLargerAreaQuestion(countries: List<CountryResponse>): Question? {
        // En caso de que venga 0.0, negativo o nulo, filtramos
        val validCountries = countries.filter { (it.area ?: 0.0) > 0 }
        if (validCountries.size < NUM_OPTIONS) return null

        val selectedCountries = validCountries.take(NUM_OPTIONS)
        
        // El correcto es el que tiene mayor área
        val correctCountry = selectedCountries.maxByOrNull { it.area ?: 0.0 } ?: return null
        
        val options = selectedCountries.map { it.name.common }.shuffled()

        return Question(
            text = "¿Cuál de estos países es el más grande por superficie?",
            imageUrl = null,
            options = options,
            correctAnswer = correctCountry.name.common,
            category = "ÁREA"
        )
    }

    // Crea una pregunta de "¿Qué país tiene más población?"
    private fun createLargerPopulationQuestion(countries: List<CountryResponse>): Question? {
        val validCountries = countries.filter { (it.population ?: 0) > 0 }
        if (validCountries.size < NUM_OPTIONS) return null

        val selectedCountries = validCountries.take(NUM_OPTIONS)
        
        val correctCountry = selectedCountries.maxByOrNull { it.population ?: 0 } ?: return null
        
        val options = selectedCountries.map { it.name.common }.shuffled()

        return Question(
            text = "¿Cuál de estos países tiene mayor población?",
            imageUrl = null,
            options = options,
            correctAnswer = correctCountry.name.common,
            category = "POBLACIÓN"
        )
    }

    // Crea una pregunta "Inversa" de Capital: "París es la capital de..."
    private fun createReverseCapitalQuestion(countries: List<CountryResponse>): Question? {
        val countriesWithCapital = countries.filter { !it.capital.isNullOrEmpty() }
        if (countriesWithCapital.size < NUM_OPTIONS) return null

        val selectedCountries = countriesWithCapital.take(NUM_OPTIONS)
        
        // Elegimos uno como "pregunta"
        val questionCountry = selectedCountries[0]
        val capitalName = questionCountry.capital?.get(0) ?: return null

        val options = selectedCountries.map { it.name.common }.shuffled()

        return Question(
            text = "$capitalName es la capital de...",
            imageUrl = null,
            options = options,
            correctAnswer = questionCountry.name.common,
            category = "CAPITALES"
        )
    }
}