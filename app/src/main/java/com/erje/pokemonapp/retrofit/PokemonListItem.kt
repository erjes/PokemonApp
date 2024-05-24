package com.erje.pokemonapp.retrofit

data class PokemonListItem(
    val id: Int,
    val name: String,
    val url: String,
    val nickname: String,
    val message: String,
    val front_default: String,
)