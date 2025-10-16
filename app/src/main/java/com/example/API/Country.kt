package com.example.API

data class Country(
    val name: Name,
    val capital: List<String>?,
    val flags: Flags,
    val region: String?,
    val subregion: String?,
    val languages: Map<String, String>?,
    val currencies: Map<String, Currency>?,
    val borders: List<String>?,
    val population: Int?,
    val area: Double?,
    val timezones: List<String>?
)

data class Name(
    val common: String,
    val official: String
)

data class Flags(
    val png: String,
    val svg: String
)

data class Currency(
    val name: String,
    val symbol: String
)
