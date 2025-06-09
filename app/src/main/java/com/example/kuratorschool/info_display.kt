package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class info_display : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info_display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener{
            val intent = Intent(this, stud_menu::class.java)
            startActivity(intent)
        }
        val stip: TextView = findViewById(R.id.stip)
        stip.setOnClickListener {
            val intent = Intent(this, info_stipendiya::class.java)
            startActivity(intent)
        }
    }
}