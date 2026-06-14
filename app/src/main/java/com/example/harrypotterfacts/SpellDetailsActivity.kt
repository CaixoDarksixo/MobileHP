package com.example.harrypotterfacts

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SpellDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spell_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tv_detail_name)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvDesc = findViewById<TextView>(R.id.tv_detail_desc)
        val btnBack = findViewById<Button>(R.id.btn_back)

        // Recupera os dados passados pelo Intent no passo 3
        val spellName = intent.getStringExtra("SPELL_NAME") ?: "Desconhecido"
        val spellDesc = intent.getStringExtra("SPELL_DESC") ?: "Sem descrição disponível."

        // Preenche a tela
        tvName.text = spellName
        tvDesc.text = spellDesc

        btnBack.setOnClickListener {
            finish() // Destrói esta Activity, retornando à lista
        }
    }
}