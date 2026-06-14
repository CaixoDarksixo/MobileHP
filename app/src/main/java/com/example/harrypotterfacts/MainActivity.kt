package com.example.harrypotterfacts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCharacterById = findViewById<MaterialCardView>(R.id.card_character_by_id)
        val btnStaff = findViewById<MaterialCardView>(R.id.card_staff)
        val btnStudentsByHouse = findViewById<MaterialCardView>(R.id.card_students_by_house)
        val btnSpells = findViewById<MaterialCardView>(R.id.card_spells)
        val btnExit = findViewById<MaterialButton>(R.id.btn_exit)

        btnCharacterById.setOnClickListener {
            val intent = Intent(this, CharacterByIdActivity::class.java)
            startActivity(intent)
        }

        btnStaff.setOnClickListener {
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }

        btnStudentsByHouse.setOnClickListener {
            val intent = Intent(this, StudentsByHouseActivity::class.java)
            startActivity(intent)
        }

        btnSpells.setOnClickListener {
            val intent = Intent(this, SpellsActivity::class.java)
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            finish()
        }
    }
}
