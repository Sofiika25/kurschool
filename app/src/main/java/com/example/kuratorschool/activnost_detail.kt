package com.example.kuratorschool

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activnost_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activnost_detail)

        val btnBack: TextView = findViewById(R.id.btn_back)
        val activityName: TextView = findViewById(R.id.activity_name)
        val activityDate: TextView = findViewById(R.id.activity_date)
        val participantsList: ListView = findViewById(R.id.participants_list)

        val name = intent.getStringExtra("activity_name") ?: "Неизвестная активность"
        val date = intent.getStringExtra("activity_date") ?: "Неизвестная дата"
        val participants = intent.getStringArrayListExtra("participants") ?: arrayListOf("Нет данных")

        activityName.text = name
        activityDate.text = "Дата: $date"

        val adapter = ArrayAdapter(this, R.layout.item_participant, R.id.practicant_name, participants)
        participantsList.adapter = adapter

        participantsList.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }
    }
}
