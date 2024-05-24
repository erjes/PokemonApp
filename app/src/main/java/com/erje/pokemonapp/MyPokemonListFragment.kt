package com.erje.pokemonapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erje.pokemonapp.retrofit.ApiInterface
import com.erje.pokemonapp.retrofit.PokemonListItem
import com.erje.pokemonapp.retrofit.PokemonListResponse
import com.erje.pokemonapp.retrofit.UrlClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPokemonListFragment : Fragment(), MyPokemonAdapter.OnPokemonActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_pokemon_list, container, false)
        recyclerView = view.findViewById(R.id.pokemon_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyPokemonAdapter(this)
        recyclerView.adapter = adapter
        loadPokemons()
        return view
    }

    private fun loadPokemons() {
        val service = UrlClient.backend.create(ApiInterface::class.java)
        val call = service.getCatchedPokemons()

        call.enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(
                call: Call<PokemonListResponse>,
                response: Response<PokemonListResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { pokemonListResponse ->
                        adapter.setPokemons(pokemonListResponse.results)
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonListResponse>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    override fun onRenamePokemon(pokemon: PokemonListItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Rename Pokémon")

        val input = EditText(requireContext())
        input.hint = "Enter new nickname"
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val nickname = input.text.toString()
            if (nickname.isNotEmpty()) {
                renamePokemon(pokemon, nickname)
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun renamePokemon(pokemon: PokemonListItem, nickname: String) {
        val service = UrlClient.backend.create(ApiInterface::class.java)
        val call = service.renamePokemon(pokemon.id, nickname)

        call.enqueue(object : Callback<PokemonListItem> {
            override fun onResponse(call: Call<PokemonListItem>, response: Response<PokemonListItem>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Renamed to $nickname", Toast.LENGTH_SHORT).show()
                    loadPokemons()
                } else {
                    Toast.makeText(requireContext(), "Failed to rename", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonListItem>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    override fun onReleasePokemon(pokemon: PokemonListItem) {
        releasePokemon(pokemon)
    }

    private fun releasePokemon(pokemon: PokemonListItem) {
        val service = UrlClient.backend.create(ApiInterface::class.java)
        val call = service.releasePokemon(pokemon.id)

        call.enqueue(object : Callback<PokemonListItem> {
            override fun onResponse(call: Call<PokemonListItem>, response: Response<PokemonListItem>) {
                if (response.isSuccessful) {
                    response.body()?.let { pokemonListResponse ->
                    Toast.makeText(requireContext(), "${pokemonListResponse.message} ", Toast.LENGTH_SHORT).show()
                    loadPokemons()
                    }

                } else {
                    Toast.makeText(requireContext(), "Failed to release", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonListItem>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}
