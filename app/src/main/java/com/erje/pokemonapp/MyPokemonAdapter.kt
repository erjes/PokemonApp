package com.erje.pokemonapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erje.pokemonapp.retrofit.PokemonListItem

class MyPokemonAdapter(private val listener: OnPokemonActionListener) : RecyclerView.Adapter<MyPokemonAdapter.PokemonViewHolder>() {
    private var pokemons: List<PokemonListItem> = ArrayList()

    interface OnPokemonActionListener {
        fun onRenamePokemon(pokemon: PokemonListItem)
        fun onReleasePokemon(pokemon: PokemonListItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_item_pokemon, parent, false)
        return PokemonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val myPokemon = pokemons[position]
        holder.textViewName.text = myPokemon.name
        holder.textViewnickName.text = "Nickname : " + myPokemon.nickname
        Glide.with(holder.itemView.context)
            .load(myPokemon.front_default)
            .into(holder.imageView)

        holder.buttonRename.setOnClickListener {
            listener.onRenamePokemon(myPokemon)
        }

        holder.buttonRelease.setOnClickListener {
            listener.onReleasePokemon(myPokemon)
        }
    }

    override fun getItemCount() = pokemons.size

    fun setPokemons(pokemons: List<PokemonListItem>) {
        this.pokemons = pokemons
        notifyDataSetChanged()
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pokemonImage)
        val textViewName: TextView = itemView.findViewById(R.id.pokemonName)
        val textViewnickName: TextView = itemView.findViewById(R.id.nickName)
        val buttonRename: Button = itemView.findViewById(R.id.buttonRename)
        val buttonRelease: Button = itemView.findViewById(R.id.buttonRelease)
    }
}
