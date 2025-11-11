// En este archivo se define el repositorio Singleton para manejar las operaciones de red (API).
// Sigue el patrón del proyecto de referencia.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp.api 

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio Singleton que maneja las operaciones de red (API).
object CountryRepository {

    // Función para obtener la lista de países desde la API.
    suspend fun fetchCountries(): Result<List<CountryResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Llama al cliente de Retrofit
                val countries = RetrofitClient.api.getAllCountries()

                // 2. Si la llamada es exitosa, envuelve la lista en Result.success
                Result.success(countries)

            } catch (e: Exception) {
                // 3. Si ocurre cualquier error (sin internet, JSON malformado, etc.),
                // envuelve la excepción en Result.failure
                Result.failure(e)
            }
        }
    }
}