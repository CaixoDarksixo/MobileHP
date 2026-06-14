package com.example.harrypotterfacts

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.harrypotterfacts.api.RetrofitClient
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterByIdActivity : AppCompatActivity() {
    private lateinit var etCharacterId: TextInputEditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var cardResult: MaterialCardView
    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvSpecies: TextView
    private lateinit var tvHouse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_character_by_id)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tv_title)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialização dos componentes
        etCharacterId = findViewById(R.id.et_character_id)
        btnSearch = findViewById(R.id.btn_search)
        progressBar = findViewById(R.id.progress_bar)
        cardResult = findViewById(R.id.card_result)
        ivPhoto = findViewById(R.id.iv_character_photo)
        tvName = findViewById(R.id.tv_name)
        tvSpecies = findViewById(R.id.tv_species)
        tvHouse = findViewById(R.id.tv_house)

        btnSearch.setOnClickListener {
            val id = etCharacterId.text.toString().trim()
            if (id.isNotEmpty()) {
                searchCharacter(id)
            } else {
                Toast.makeText(this, "Por favor, digite um ID válido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchCharacter(id: String) {
        // Mostra o ProgressBar e esconde o resultado anterior (se houver)
        progressBar.visibility = View.VISIBLE
        cardResult.visibility = View.GONE

        // Inicia a corrotina amarrada ao ciclo de vida desta Activity
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Faz a requisição de rede na thread de IO
                val response =
                    withContext(Dispatchers.IO) {
                        RetrofitClient.api.getCharacterById(id)
                    }

                val character = response.firstOrNull()

                if (character != null) {
                    // Preenche os dados do personagem
                    tvName.text = character.name
                    tvSpecies.text = "Species: ${character.species.ifEmpty { "Unknown" }}"
                    tvHouse.text = "House: ${character.house.ifEmpty { "Unknown" }}"

                    // Carrega a imagem com Picasso (trata o caso de imagem vazia na API)
                    if (character.image.isNotEmpty()) {
                        Picasso.get()
                            .load(character.image)
                            .into(ivPhoto)
                    } else {
                        // Se o personagem não tiver foto na API, limpa o ImageView ou pode colocar um placeholder
                        ivPhoto.setImageDrawable(null)
                    }

                    // Exibe o Card com os resultados
                    cardResult.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@CharacterByIdActivity, "Personagem não encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Trata erro de rede, queda de internet, etc.
                Toast.makeText(this@CharacterByIdActivity, "Erro ao buscar personagem. Tente novamente.", Toast.LENGTH_SHORT).show()
            } finally {
                // Esconde o ProgressBar independentemente de sucesso ou erro
                progressBar.visibility = View.GONE
            }
        }
    }
}
