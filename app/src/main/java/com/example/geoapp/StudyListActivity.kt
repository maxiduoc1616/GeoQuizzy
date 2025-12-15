package com.example.geoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geoapp.api.RetrofitClient
import com.example.geoapp.databinding.ActivityStudyListBinding
import com.example.geoapp.db.QuizDbHelper
import com.example.geoapp.db.StudyItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyListBinding
    private lateinit var dbHelper: QuizDbHelper
    private lateinit var adapter: StudyAdapter
    private var flagsMap = mapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = QuizDbHelper(this)
        binding.rvStudyList.layoutManager = LinearLayoutManager(this)

        loadData()

        binding.btnPractice.setOnClickListener {
            startPracticeQuiz()
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, CountryListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            
            // 1. Cargar lista de la BD local
            val studyList = withContext(Dispatchers.IO) {
                dbHelper.getStudyList()
            }

            // 2. Cargar datos de la API para las banderas (si no se han cargado aún)
            if (flagsMap.isEmpty()) {
                try {
                    val countries = withContext(Dispatchers.IO) {
                        RetrofitClient.api.getAllCountries()
                    }
                    flagsMap = countries.associate { it.name.common to it.flags.png }
                } catch (e: Exception) {
                    Toast.makeText(this@StudyListActivity, "Error cargando imágenes", Toast.LENGTH_SHORT).show()
                }
            }

            // 3. Configurar adapter
            if (studyList.isEmpty()) {
                binding.tvEmptyState.visibility = android.view.View.VISIBLE
                binding.rvStudyList.visibility = android.view.View.GONE
            } else {
                binding.tvEmptyState.visibility = android.view.View.GONE
                binding.rvStudyList.visibility = android.view.View.VISIBLE
                
                adapter = StudyAdapter(
                    studyList, 
                    flagsMap,
                    onEditClick = { item -> showEditPriorityDialog(item) },
                    onDeleteClick = { item -> deleteItem(item) }
                )
                binding.rvStudyList.adapter = adapter
            }
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    private fun showEditPriorityDialog(item: StudyItem) {
        val priorities = arrayOf("Baja", "Media", "Alta")
        // Mapear prioridad numérica (1,2,3) a índice (0,1,2)
        val currentSelection = item.priority - 1

        AlertDialog.Builder(this)
            .setTitle("Editar Prioridad: ${item.countryName}")
            .setSingleChoiceItems(priorities, currentSelection) { dialog, which ->
                val newPriority = which + 1
                dbHelper.updateStudyItemPriority(item.countryName, newPriority)
                loadData() // Recargar lista
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteItem(item: StudyItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Estás seguro de quitar a ${item.countryName} de tu lista?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.deleteStudyItem(item.countryName)
                loadData()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun startPracticeQuiz() {
        // Verificar si hay suficientes preguntas
        val list = dbHelper.getStudyList()
        if (list.size < 4) {
            Toast.makeText(this, "Necesitas al menos 4 países en tu lista para practicar.", Toast.LENGTH_LONG).show()
            return
        }
        
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("EXTRA_MODE", "STUDY")
        startActivity(intent)
    }
    
    override fun onResume() {
        super.onResume()
        // Recargar si volvemos (por si agregaron algo en explore y volvieron aqui directamente en el stack, aunque el flujo es Home -> Explore)
        // Pero útil si implementamos back navigation
        loadData()
    }
}
