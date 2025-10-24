package com.example.geoapp.api // Asegúrate que este sea tu paquete

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio Singleton para manejar las operaciones de red (API).
 * Sigue el patrón del proyecto de referencia:
 * - Es un 'object' (Singleton).
 * - Usa 'suspend' y 'withContext(Dispatchers.IO)' para ejecutar en hilo de fondo.
 * - Devuelve 'Result<T>' para un manejo seguro de errores.
 */
object CountryRepository {

    /**
     * Obtiene la lista de todos los países desde la API.
     * Esta es la única función que la UI (Activity/ViewModel) llamará.
     */
    suspend fun fetchCountries(): Result<List<CountryResponse>> {
        // Ejecuta la llamada a la red en el hilo de IO (Input/Output)
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

    // NOTA: No necesitamos un "insertCountry" porque nuestra API es de solo lectura (GET).
}