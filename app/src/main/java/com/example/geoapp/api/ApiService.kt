package com.example.geoapp.api

import retrofit2.http.GET

/**
 * Interfaz de Retrofit que define los endpoints de la API.
 */
interface ApiService {

    /**
     * Obtiene la lista de todos los países.
     * Es una función 'suspend' para ser llamada desde una Coroutine.
     */
    @GET("all?fields=name,capital,flags,continents") // <-- Traemos solo los campos que necesitamos
    suspend fun getAllCountries(): List<CountryResponse>
}