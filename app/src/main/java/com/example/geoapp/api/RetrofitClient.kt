package com.example.geoapp.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Objeto Singleton para crear y configurar la instancia de Retrofit.
 */
object RetrofitClient {

    // URL base de la API
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    // 1. Configura Moshi para que funcione con Kotlin
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // 2. Configura Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // <-- Le dice que use Moshi
        .build()

    /**
     * 3. Crea una instancia 'lazy' del ApiService.
     * 'lazy' significa que solo se creará la primera vez que la necesitemos.
     */
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}