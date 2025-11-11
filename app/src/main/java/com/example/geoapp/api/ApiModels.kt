// En este archivo se definen los modelos de datos que representan la respuesta de la API REST Countries.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp.api

import com.squareup.moshi.Json

// Modelo de datos para la respuesta de la API REST Countries

// Representa un país con su nombre, capital, bandera y continente
data class CountryResponse(
    @field:Json(name = "name") val name: Name,
    @field:Json(name = "capital") val capital: List<String>?, // La capital es una lista
    @field:Json(name = "flags") val flags: Flag,
    @field:Json(name = "continents") val continents: List<String>
)

// Objeto anidado para el nombre del país
data class Name(
    @field:Json(name = "common") val common: String
)

// Objeto anidado para las banderas
data class Flag(
    @field:Json(name = "png") val png: String // URL de la imagen PNG de la bandera
)