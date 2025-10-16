package com.example.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("name/{country}")
    fun getCountryInfo(
        @Path("country") country: String,
        @Query("fullText") fullText: Boolean = true,
        @Query("fields") fields: String = "name,capital,flags,region,subregion,languages,currencies,borders,population,area,timezones"
    ): Call<List<Country>>
}
