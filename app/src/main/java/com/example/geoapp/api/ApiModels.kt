package com.example.geoapp.api

import com.squareup.moshi.Json

/**
 * Modelos de datos (DTOs) que mapean la respuesta JSON de la API REST Countries.
 * Solo incluimos los campos que realmente vamos a usar.
 */

// Este es el objeto principal en la lista de respuesta
data class CountryResponse(
    @field:Json(name = "name") val name: Name,
    @field:Json(name = "capital") val capital: List<String>?, // La capital es una lista
    @field:Json(name = "flags") val flags: Flag,
    @field:Json(name = "continents") val continents: List<String>
)

// Objeto anidado para el nombre
data class Name(
    @field:Json(name = "common") val common: String
)

// Objeto anidado para las banderas
data class Flag(
    @field:Json(name = "png") val png: String // URL de la imagen PNG de la bandera
)