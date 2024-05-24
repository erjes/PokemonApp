package com.erje.pokemonapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erje.pokemonapp.retrofit.PokemonListItem

class PokemonAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private var pokemons: List<PokemonListItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = pokemons[position]
        holder.textViewName.text = currentItem.name
        // Here we can set a placeholder image or fetch the sprite if available
        Glide.with(holder.itemView.context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getIdFromUrl(currentItem.url)}.png")
            .into(holder.imageView)
    }

    override fun getItemCount() = pokemons.size

    fun setPokemons(pokemons: List<PokemonListItem>) {
        this.pokemons = pokemons
        notifyDataSetChanged()
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.pokemonImage)
        val textViewName: TextView = itemView.findViewById(R.id.pokemonName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(pokemons[position])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pokemon: PokemonListItem)
    }

    private fun getIdFromUrl(url: String): String {
        return url.split("/".toRegex()).dropLast(1).last()
    }
}
