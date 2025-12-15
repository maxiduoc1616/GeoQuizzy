package com.example.geoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geoapp.api.CountryResponse

class CountryAdapter(
    private val countries: List<CountryResponse>,
    private val onAddClick: (CountryResponse) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private var filteredCountries: List<CountryResponse> = countries

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFlag: ImageView = itemView.findViewById(R.id.ivFlag)
        val tvCountryName: TextView = itemView.findViewById(R.id.tvCountryName)
        val btnAdd: ImageButton = itemView.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = filteredCountries[position]
        holder.tvCountryName.text = country.name.common

        Glide.with(holder.itemView.context)
            .load(country.flags.png)
            .into(holder.ivFlag)

        holder.btnAdd.setOnClickListener {
            onAddClick(country)
        }
    }

    override fun getItemCount() = filteredCountries.size

    fun filter(query: String) {
        filteredCountries = if (query.isEmpty()) {
            countries
        } else {
            countries.filter {
                it.name.common.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
