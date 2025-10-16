package com.example.evaluacion2

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.API.Country
import com.example.API.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {

    private lateinit var textInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Encontrar el TextView desde el layout
        textInfo = findViewById(R.id.textInfo)

        // Aplicar margen para barras del sistema (opcional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Llamar a la API
        val countryName = "colombia"
        fetchCountryInfo(countryName)
    }

    private fun fetchCountryInfo(countryName: String) {
        RetrofitClient.apiService.getCountryInfo(countryName)
            .enqueue(object : Callback<List<Country>> {
                override fun onResponse(
                    call: Call<List<Country>>,
                    response: Response<List<Country>>
                ) {
                    if (response.isSuccessful) {
                        val countries = response.body()
                        countries?.let {
                            val country = it[0]

                            val info = """
                                Nombre: ${country.name.common}
                                Capital: ${country.capital?.joinToString()}
                                Población: ${country.population}
                                Región: ${country.region}
                                Subregión: ${country.subregion}
                                Área: ${country.area} km²
                                Zona Horaria: ${country.timezones?.joinToString()}
                                Fronteras: ${country.borders?.joinToString()}
                                Lenguajes: ${country.languages?.values?.joinToString()}
                                Monedas: ${country.currencies?.values?.joinToString { c -> "${c.name} (${c.symbol})" }}
                            """.trimIndent()

                            textInfo.text = info
                        }
                    } else {
                        Log.e("API", "Respuesta fallida: ${response.message()}")
                        textInfo.text = "Error al obtener datos: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                    textInfo.text = "Fallo de red: ${t.message}"
                }

            })
    }
}
