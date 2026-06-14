package com.example.harrypotterfacts

import android.os.Bundle
import android.view.View
import android.widget.Button
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffActivity : AppCompatActivity() {
    private lateinit var etStaffName: TextInputEditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var cardResult: MaterialCardView
    private lateinit var tvName: TextView
    private lateinit var tvAlternateNames: TextView
    private lateinit var tvSpecies: TextView
    private lateinit var tvHouse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_staff)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tv_title)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialização dos componentes
        etStaffName = findViewById(R.id.et_staff_name)
        btnSearch = findViewById(R.id.btn_search)
        progressBar = findViewById(R.id.progress_bar)
        cardResult = findViewById(R.id.card_result)
        tvName = findViewById(R.id.tv_name)
        tvAlternateNames = findViewById(R.id.tv_alternate_names)
        tvSpecies = findViewById(R.id.tv_species)
        tvHouse = findViewById(R.id.tv_house)

        btnSearch.setOnClickListener {
            val query = etStaffName.text.toString().trim()
            if (query.isNotEmpty()) {
                searchStaff(query)
            } else {
                Toast.makeText(this, "Por favor, digite o nome de um professor.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchStaff(query: String) {
        progressBar.visibility = View.VISIBLE
        cardResult.visibility = View.GONE

        // Inicia a corrotina amarrada ao ciclo de vida desta Activity
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Faz a requisição de rede na thread de IO
                val staffList =
                    withContext(Dispatchers.IO) {
                        RetrofitClient.api.getStaff()
                    }

                // Busca o professor pelo nome ou nome alternativo (case-insensitive)
                val professor = staffList.firstOrNull { character ->
                    character.name.contains(query, ignoreCase = true) ||
                        character.alternateNames.any { alt -> alt.contains(query, ignoreCase = true) }
                }

                if (professor != null) {
                    // Preenche os dados do professor
                    tvName.text = "Name: ${professor.name}"
                    tvAlternateNames.text = "Alternate names: ${professor.alternateNames.joinToString(", ").ifEmpty { "None" }}"
                    tvSpecies.text = "Species: ${professor.species.ifEmpty { "Unknown" }}"
                    tvHouse.text = "House: ${professor.house.ifEmpty { "Unknown" }}"

                    cardResult.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@StaffActivity, "Professor não encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Trata erro de rede, queda de internet, etc.
                Toast.makeText(this@StaffActivity, "Erro ao buscar professores. Tente novamente.", Toast.LENGTH_SHORT).show()
            } finally {
                // Esconde o ProgressBar independentemente de sucesso ou erro
                progressBar.visibility = View.GONE
            }
        }
    }
}
