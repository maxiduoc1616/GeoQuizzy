package com.example.geoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geoapp.databinding.ActivityDifficultySelectionBinding

class DifficultySelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDifficultySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDifficultySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnEasy.setOnClickListener {
            startGame(5)
        }

        binding.btnMedium.setOnClickListener {
            startGame(3)
        }

        binding.btnHard.setOnClickListener {
            startGame(1)
        }
    }

    private fun startGame(lives: Int) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("EXTRA_LIVES", lives)
        startActivity(intent)
        finish() // Cierra la selección para que al volver atrás vaya al Home
    }
}
