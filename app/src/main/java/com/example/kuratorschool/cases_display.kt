package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Case
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cases_display : AppCompatActivity() {
    private lateinit var addActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var recyclerView: RecyclerView
    private lateinit var casesList: MutableList<Case>
    private lateinit var adapter: CasesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cases_display)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Лаунчер для обновления списка после возврата
        addActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                addValueEventListener()
            }
        }

        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, kur_menu::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.btn_school).setOnClickListener {
            startActivity(Intent(this, kur_menu::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.btn_group).setOnClickListener {
            startActivity(Intent(this, kur_group_menu::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.btn_info).setOnClickListener {
            startActivity(Intent(this, kur_info::class.java))
            finish()
        }

        recyclerView = findViewById(R.id.caseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        casesList = mutableListOf()
        adapter = CasesAdapter(casesList, addActivityLauncher)
        recyclerView.adapter = adapter

        addValueEventListener()
    }

    private fun addValueEventListener() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Cases")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                casesList.clear()
                for (caseSnapshot in snapshot.children) {
                    val case = caseSnapshot.getValue(Case::class.java)
                    if (case != null && (case.userAnswers == null || case.userAnswers.isEmpty())) {
                        casesList.add(case)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Ошибка: ${error.message}")
            }
        })
    }
}
