package com.erje.pokemonapp.retrofit


data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>,
    val status: String,
    val message: String,
)


