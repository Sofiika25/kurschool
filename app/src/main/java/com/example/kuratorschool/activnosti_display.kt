package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Adapter.ActivityAdapter
import com.example.kuratorschool.Models.Activity
import com.example.kuratorschool.Models.Participant
import com.google.firebase.database.*


class activnosti_display : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private val activitiesList = mutableListOf<Activity>()
    private lateinit var addActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activnosti_display)

        recyclerView = findViewById(R.id.activnostresycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        activityAdapter = ActivityAdapter(this, activitiesList)
        recyclerView.adapter = activityAdapter


        database = FirebaseDatabase.getInstance().getReference("ActivityTracking")
        addActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loadActivities()
            }
        }
        loadActivities()
    }

    private fun loadActivities() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activitiesList.clear()
                val btnBack: TextView = findViewById(R.id.btn_back)
                for (activitySnapshot in snapshot.children) {
                    val activityId = activitySnapshot.child("activity_id").value.toString()
                    val curatorId = activitySnapshot.child("curator_id").value.toString()
                    val date = activitySnapshot.child("date").value.toString()
                    val groupId = activitySnapshot.child("group_id").getValue(Int::class.java) ?: 0
                    val nameActivity = activitySnapshot.child("name_activity").value.toString()

                    val participantsList = mutableListOf<Participant>()
                    val participantsSnapshot = activitySnapshot.child("participants")
                    for (participant in participantsSnapshot.children) {
                        val fullName = participant.child("full_name").value.toString()
                        val userId = participant.child("user_id").value.toString()
                        participantsList.add(Participant(fullName, userId))
                    }

                    val activity = Activity(activityId, curatorId, date, groupId, nameActivity, participantsList)
                    activitiesList.add(activity)
                }

                val addActivnostButton = findViewById<Button>(R.id.addActivButton)
                addActivnostButton.setOnClickListener {
                    val intent = Intent(this@activnosti_display, add_activnost::class.java)
                    addActivityLauncher.launch(intent)
                }


                btnBack.setOnClickListener {
                    val intent = Intent(this@activnosti_display, kur_group_menu::class.java)
                    startActivity(intent)
                }

                activityAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Ошибка загрузки данных: ${error.message}")
            }
        })
    }
}
