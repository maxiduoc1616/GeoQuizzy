package com.example.geoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoapp.api.RetrofitClient
import com.example.geoapp.databinding.ActivityCountryListBinding
import com.example.geoapp.db.QuizDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountryListBinding
    private lateinit var dbHelper: QuizDbHelper
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = QuizDbHelper(this)
        binding.rvCountries.layoutManager = LinearLayoutManager(this)

        setupSearch()
        loadCountries()
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::adapter.isInitialized) {
                    adapter.filter(newText ?: "")
                }
                return true
            }
        })
    }

    private fun loadCountries() {
        lifecycleScope.launch {
            try {
                // Mostrar progress bar
                binding.progressBar.visibility = android.view.View.VISIBLE
                binding.rvCountries.visibility = android.view.View.GONE

                // Llamada a la API en IO dispatcher
                val countries = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getAllCountries()
                }

                // Configurar adapter en Main thread
                adapter = CountryAdapter(countries) { country ->
                    addToStudyList(country.name.common)
                }
                binding.rvCountries.adapter = adapter

                binding.progressBar.visibility = android.view.View.GONE
                binding.rvCountries.visibility = android.view.View.VISIBLE

            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@CountryListActivity, "Error al cargar países: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun addToStudyList(countryName: String) {
        if (dbHelper.isCountryInStudyList(countryName)) {
            Toast.makeText(this, "$countryName ya está en tu lista", Toast.LENGTH_SHORT).show()
        } else {
            val id = dbHelper.insertStudyItem(countryName, 2) // Prioridad Media por defecto (2)
            if (id != -1L) {
                Toast.makeText(this, "$countryName agregado a tu lista", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
