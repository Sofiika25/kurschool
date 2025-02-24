package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Adapter.TestsAdapter
import com.example.kuratorschool.Models.Test
import com.example.kuratorschool.Models.TestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class tests_display : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TestsAdapter
    private val testsList = mutableListOf<Test>()


    private var resultsMap = mutableMapOf<String, TestResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tests_display)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, kur_menu::class.java))
            finish()
        }
        val btnSchool: ImageView = findViewById(R.id.btn_school)
        btnSchool.setOnClickListener {
            startActivity(Intent(this, kur_menu::class.java))
            finish()
        }
        val btnGroup: ImageView = findViewById(R.id.btn_group)
        btnGroup.setOnClickListener {
            startActivity(Intent(this, kur_group_menu::class.java))
            finish()
        }
        val btnInfo: ImageView = findViewById(R.id.btn_info)
        btnInfo.setOnClickListener {
            startActivity(Intent(this, kur_info::class.java))
            finish()
        }


        recyclerView = findViewById(R.id.recycler_view_tests)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TestsAdapter(emptyList(), resultsMap) { selectedTest ->

            if (resultsMap.containsKey(selectedTest.id)) {
                Toast.makeText(this, "Тест уже выполнен", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, test1::class.java)
                intent.putExtra("testId", selectedTest.id)
                startActivity(intent)
            }
        }
        recyclerView.adapter = adapter

        loadTestsFromFirebase()
        loadResultsForUser()
    }

    private fun loadTestsFromFirebase() {
        val ref = FirebaseDatabase.getInstance().getReference("Tests")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testsList.clear()
                for (childSnapshot in snapshot.children) {
                    val test = childSnapshot.getValue(Test::class.java)
                    if (test != null && test.isAccessible) {
                        testsList.add(test)
                    }
                }
                adapter.updateList(testsList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadResultsForUser() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("Results")

        ref.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    resultsMap.clear()
                    for (childSnapshot in snapshot.children) {
                        val result = childSnapshot.getValue(TestResult::class.java)
                        if (result != null) {
                            resultsMap[result.testId] = result
                        }
                    }
                    adapter.updateResults(resultsMap)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
