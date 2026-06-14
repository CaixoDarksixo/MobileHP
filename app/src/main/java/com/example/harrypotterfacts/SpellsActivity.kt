package com.example.harrypotterfacts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotterfacts.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpellsActivity : AppCompatActivity() {
    private lateinit var rvSpells: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var spellAdapter: SpellAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spells)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tv_title)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvSpells = findViewById(R.id.rv_spells)
        progressBar = findViewById(R.id.progress_bar)

        rvSpells.layoutManager = LinearLayoutManager(this)

        // Passamos a lambda de clique que vai abrir a tela de Detalhes
        spellAdapter =
            SpellAdapter(emptyList()) { spell ->
                val intent =
                    Intent(this, SpellDetailsActivity::class.java).apply {
                        putExtra("SPELL_NAME", spell.name)
                        putExtra("SPELL_DESC", spell.description)
                    }
                startActivity(intent)
            }
        rvSpells.adapter = spellAdapter

        // Chamamos a API automaticamente ao abrir a tela
        fetchSpells()
    }

    private fun fetchSpells() {
        progressBar.visibility = View.VISIBLE
        rvSpells.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val spellsList =
                    withContext(Dispatchers.IO) {
                        RetrofitClient.api.getSpells()
                    }

                if (spellsList.isNotEmpty()) {
                    spellAdapter.updateData(spellsList)
                    rvSpells.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@SpellsActivity, "Nenhum feitiço encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SpellsActivity, "Erro ao carregar os feitiços.", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
