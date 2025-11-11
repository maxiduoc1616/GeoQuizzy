// En este archivo se define el cliente Retrofit como un Singleton para realizar solicitudes HTTP a la API.
// Utiliza Moshi como convertidor JSON para mapear las respuestas a objetos Kotlin.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Cliente Retrofit Singleton configurado con Moshi para manejar las solicitudes HTTP a la API REST.
object RetrofitClient {

    // URL base de la API
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    // Configuración de Moshi
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Configuración de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // <-- Le dice que use Moshi
        .build()

    // Exposición de la interfaz ApiService
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}