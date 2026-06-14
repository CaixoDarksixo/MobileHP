package com.example.harrypotterfacts

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioGroup
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

class StudentsByHouseActivity : AppCompatActivity() {
    private lateinit var rgHouses: RadioGroup
    private lateinit var rvStudents: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_by_house)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tv_title)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rgHouses = findViewById(R.id.rg_houses)
        rvStudents = findViewById(R.id.rv_students)
        progressBar = findViewById(R.id.progress_bar)

        // Configuração do RecyclerView
        rvStudents.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(emptyList()) // Começa com lista vazia
        rvStudents.adapter = studentAdapter

        // Listener para identificar mudança nos RadioButtons
        rgHouses.setOnCheckedChangeListener { _, checkedId ->
            val house =
                when (checkedId) {
                    R.id.rb_gryffindor -> "gryffindor"
                    R.id.rb_slytherin -> "slytherin"
                    R.id.rb_ravenclaw -> "ravenclaw"
                    R.id.rb_hufflepuff -> "hufflepuff"
                    else -> ""
                }

            if (house.isNotEmpty()) {
                fetchStudents(house)
            }
        }
    }

    private fun fetchStudents(house: String) {
        progressBar.visibility = View.VISIBLE
        rvStudents.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val studentsList =
                    withContext(Dispatchers.IO) {
                        RetrofitClient.api.getStudentsByHouse(house)
                    }

                if (studentsList.isNotEmpty()) {
                    studentAdapter.updateData(studentsList)
                    rvStudents.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@StudentsByHouseActivity, "Nenhum estudante encontrado.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@StudentsByHouseActivity, "Erro ao buscar estudantes.", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
