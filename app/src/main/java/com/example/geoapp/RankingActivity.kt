// Esta actividad muestra el ranking de puntajes altos obtenidos en los quizzes. Utiliza un RecyclerView para listar los puntajes 
// almacenados en la base de datos local.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoapp.databinding.ActivityRankingBinding
import com.example.geoapp.db.QuizDbHelper
import com.example.geoapp.db.Score

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding
    private lateinit var dbHelper: QuizDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la Toolbar con el botón de "hacia atrás"
        setSupportActionBar(binding.toolbarRanking)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = QuizDbHelper(this)
        setupRecyclerView()
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView() {
        val highScores: List<Score> = dbHelper.getHighScores()

        // Si no hay puntajes, ocultamos la tarjeta del ranking
        if (highScores.isEmpty()) {
            binding.cardViewRankingList.visibility = View.GONE
            binding.tvEmptyRanking.visibility = View.VISIBLE
        } else {
            // Si hay puntajes, los mostramos en el RecyclerView
            binding.cardViewRankingList.visibility = View.VISIBLE
            binding.tvEmptyRanking.visibility = View.GONE

            val rankingAdapter = RankingAdapter(highScores)
            binding.rvRanking.apply {
                layoutManager = LinearLayoutManager(this@RankingActivity)
                adapter = rankingAdapter
                setHasFixedSize(true)
            }
        }
    }
}