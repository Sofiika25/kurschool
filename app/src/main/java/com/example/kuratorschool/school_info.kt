package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class school_info : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_school_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener{
            val intent = Intent(this, stud_menu::class.java)
            startActivity(intent)
            finish()
        }
        val zayavkaBtn: Button = findViewById(R.id.zayavkaButton)
        zayavkaBtn.setOnClickListener{
            val intent = Intent(this, zayavka_for_school::class.java)
            startActivity(intent)
            finish()
        }
    }
}