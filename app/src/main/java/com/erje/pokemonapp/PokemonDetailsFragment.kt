package com.erje.pokemonappimport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.erje.pokemonapp.R
import com.erje.pokemonapp.retrofit.ApiInterface
import com.erje.pokemonapp.retrofit.Pokemon
import com.erje.pokemonapp.retrofit.PokemonListResponse
import com.erje.pokemonapp.retrofit.UrlClient
import com.erje.pokemonapp.retrofit.catchPokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailsFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewWeight: TextView
    private lateinit var textViewHeight: TextView
    private lateinit var textViewTypes: TextView
    private lateinit var textViewAbilities: TextView
    private lateinit var catchButton: Button

    private lateinit var pokemonName: String
    private lateinit var front_default: String
    private lateinit var nickname: String


    companion object {
        private const val ARG_POKEMON_URL = "pokemon_url"

        fun newInstance(url: String): PokemonDetailsFragment {
            val fragment = PokemonDetailsFragment()
            val args = Bundle()
            args.putString(ARG_POKEMON_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokemon_details
            , container, false)
        imageView = view.findViewById(R.id.imageView)
        textViewName = view.findViewById(R.id.textViewName)
        textViewWeight = view.findViewById(R.id.textViewWeight)
        textViewHeight = view.findViewById(R.id.textViewHeight)
        catchButton = view.findViewById(R.id.catchButton)

        textViewAbilities = view.findViewById(R.id.textViewAbilities)
        textViewTypes = view.findViewById(R.id.textViewTypes)

        catchButton.setOnClickListener {
            catchPokemon()
        }

        val url = arguments?.getString(ARG_POKEMON_URL)
        if (url != null) {
            loadPokemonDetails(url)
        }

        return view
    }

    private fun loadPokemonDetails(url: String) {
        val service = UrlClient.retrofit.create(ApiInterface::class.java)
        val call = service.getPokemonDetails(url)

        call.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    response.body()?.let { pokemon ->
                        textViewName.text = pokemon.name
                        pokemonName = pokemon.name
                        nickname = pokemon.name
                        front_default = pokemon.sprites.front_default
                        textViewWeight.text = "Weight: ${pokemon.weight}"
                        textViewHeight.text = "Height: ${pokemon.height}"
                        textViewTypes.text = "Types: ${pokemon.types.joinToString { it.type.name }}"
                        textViewAbilities.text = "Abilities: ${pokemon.abilities.joinToString { it.ability.name }}"
                        Glide.with(this@PokemonDetailsFragment).load(pokemon.sprites.front_default).into(imageView)
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun catchPokemon() {
        val service = UrlClient.backend.create(ApiInterface::class.java)
        val pokemon = catchPokemon(pokemonName, front_default)
        val call = service.catchPokemon(pokemon)

        call.enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(call: Call<PokemonListResponse>, response: Response<PokemonListResponse>) {
                if (response.isSuccessful) {
                    val caughtPokemon = response.body()
                    caughtPokemon?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
