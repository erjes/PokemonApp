package com.erje.pokemonapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erje.pokemonapp.PokemonAdapter
import com.erje.pokemonapp.R
import com.erje.pokemonapp.retrofit.ApiInterface
import com.erje.pokemonapp.retrofit.PokemonListItem
import com.erje.pokemonapp.retrofit.PokemonListResponse
import com.erje.pokemonapp.retrofit.UrlClient
import com.erje.pokemonappimport.PokemonDetailsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonListFragment : Fragment(), PokemonAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokemon_list, container, false)
        recyclerView = view.findViewById(R.id.pokemon_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PokemonAdapter(this)
        recyclerView.adapter = adapter
        loadPokemons()
        return view
    }

    private fun loadPokemons() {
        val service = UrlClient.retrofit.create(ApiInterface::class.java)
        val call = service.getPokemons()

        call.enqueue(object : Callback<PokemonListResponse> {
            override fun onResponse(call: Call<PokemonListResponse>, response: Response<PokemonListResponse>) {
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
            }
        })
    }

    override fun onItemClick(pokemon: PokemonListItem) {
        val fragment = PokemonDetailsFragment.newInstance(pokemon.url)
        fragmentManager?.beginTransaction()?.replace(R.id.container, fragment)
            ?.addToBackStack(null)?.commit()
    }
}
