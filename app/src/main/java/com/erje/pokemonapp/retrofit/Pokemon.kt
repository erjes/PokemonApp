package com.erje.pokemonapp.retrofit

data class Pokemon(
    val name: String,
    val front_default: String,
    val sprites: Sprites,
    val success: Boolean,
    val message: String,
    val weight: Int,
    val height: Int,
    val types: List<TypeSlot>,
    val abilities: List<AbilitySlot>
)

data class catchPokemon(
    val name: String,
    val front_default: String,
)

data class TypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String
)

data class AbilitySlot(
    val slot: Int,
    val ability: Ability
)

data class Ability(
    val name: String
)

data class Sprites(
    val front_default: String
)



