// En este archivo se define la interfaz de la API utilizando Retrofit para realizar solicitudes HTTP.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.api

import retrofit2.http.GET

// Interfaz que define los endpoints de la API REST utilizando Retrofit.
interface ApiService {

    // Endpoint para obtener la lista de todos los países con campos específicos.
    @GET("all?fields=name,capital,flags,continents")
    suspend fun getAllCountries(): List<CountryResponse>
}