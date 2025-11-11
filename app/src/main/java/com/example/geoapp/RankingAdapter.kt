// Esta clase adapta una lista de puntajes para mostrarlos en un RecyclerView en la actividad de ranking.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González


package com.example.geoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geoapp.db.Score

class RankingAdapter(private val scores: List<Score>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    // ViewHolder que contiene las vistas para cada ítem del ranking
    inner class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankTextView: TextView = itemView.findViewById(R.id.tv_rank)
        val usernameTextView: TextView = itemView.findViewById(R.id.tv_username)
        val scoreTextView: TextView = itemView.findViewById(R.id.tv_score)
    }

    // Se llama para crear una nueva vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ranking_item, parent, false)
        return RankingViewHolder(view)
    }

    // Devuelve la cantidad total de elementos en nuestra lista de datos.
    override fun getItemCount(): Int {
        return scores.size
    }

    // Vincula los datos a las vistas en el ViewHolder
    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val currentScore = scores[position]

        holder.rankTextView.text = "${position + 1}." // La posición empieza en 0, el ranking en 1
        holder.usernameTextView.text = currentScore.username
        // Asumiendo que el score es sobre 10 preguntas (lo que está por defecto actualmente)
        holder.scoreTextView.text = "${currentScore.score}/10"
    }
}