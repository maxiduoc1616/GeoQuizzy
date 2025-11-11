// Esta clase funciona como un adaptador para un RecyclerView que muestra una lista de logros en una aplicación Android.

// Evaluación Parcial 2
// Integrantes: Diego Rodríguez, Maximiliano Gangas, Bastian González

package com.example.geoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geoapp.databinding.ItemAchievementBinding
import com.example.geoapp.db.Achievement

class AchievementAdapter(private val achievements: List<Achievement>) : RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount() = achievements.size

    // ViewHolder interno para manejar la vista de cada logro
    class AchievementViewHolder(private val binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: Achievement) {
            binding.tvAchievementTitle.text = achievement.name
            binding.tvAchievementDescription.text = achievement.description
            binding.ivAchievementIcon.setImageResource(achievement.iconResId)

            if (achievement.isUnlocked) {
                binding.ivAchievementIcon.alpha = 1.0f
                binding.pbAchievementProgress.visibility = View.GONE
            } else {
                binding.ivAchievementIcon.alpha = 0.3f
                if (achievement.maxProgress > 1) {
                    binding.pbAchievementProgress.visibility = View.VISIBLE
                    binding.pbAchievementProgress.max = achievement.maxProgress
                    binding.pbAchievementProgress.progress = achievement.progress
                } else {
                    binding.pbAchievementProgress.visibility = View.GONE
                }
            }
        }
    }
}
