package com.example.geoapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geoapp.db.StudyItem

class StudyAdapter(
    private val studyList: List<StudyItem>,
    private val flagsMap: Map<String, String>, // nombre -> url
    private val onEditClick: (StudyItem) -> Unit,
    private val onDeleteClick: (StudyItem) -> Unit
) : RecyclerView.Adapter<StudyAdapter.StudyViewHolder>() {

    inner class StudyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFlag: ImageView = itemView.findViewById(R.id.ivFlag)
        val tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
        val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_country, parent, false)
        return StudyViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudyViewHolder, position: Int) {
        val item = studyList[position]
        holder.tvCountryName.text = item.countryName

        // Cargar bandera si existe
        val flagUrl = flagsMap[item.countryName]
        if (flagUrl != null) {
            Glide.with(holder.itemView.context)
                .load(flagUrl)
                .into(holder.ivFlag)
        } else {
            holder.ivFlag.setImageResource(R.drawable.ic_launcher_background) // Fallback
        }

        // Configurar prioridad
        when (item.priority) {
            3 -> {
                holder.tvPriority.text = "ALTA"
                holder.tvPriority.setBackgroundColor(Color.parseColor("#F44336")) // Rojo
            }
            2 -> {
                holder.tvPriority.text = "MEDIA"
                holder.tvPriority.setBackgroundColor(Color.parseColor("#FF9800")) // Naranja
            }
            else -> {
                holder.tvPriority.text = "BAJA"
                holder.tvPriority.setBackgroundColor(Color.parseColor("#4CAF50")) // Verde
            }
        }

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = studyList.size
}
