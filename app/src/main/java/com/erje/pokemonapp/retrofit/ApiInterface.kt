package com.erje.pokemonapp.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiInterface {

    @GET("pokemon/?limit=1000")
    fun getPokemons(): Call<PokemonListResponse>

    @GET
    fun getPokemonDetails(@Url url: String): Call<Pokemon>

    @GET("pokemons")
    fun getCatchedPokemons(): Call<PokemonListResponse>

    @POST("pokemon/catch")
    fun catchPokemon(@Body pokemon: catchPokemon): Call<PokemonListResponse>

    @PUT("pokemon/rename/{id}")
    @FormUrlEncoded
    fun renamePokemon(@Path("id") id: Int, @Field("nickname") nickname: String): Call<PokemonListItem>

    @DELETE("pokemon/release/{id}")
    fun releasePokemon(@Path("id") id: Int): Call<PokemonListItem>


}
