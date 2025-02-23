package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class kur_menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kur_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnExit: TextView = findViewById(R.id.btn_exit)
        btnExit.setOnClickListener{
            val intent = Intent(this, Authorization::class.java)
            startActivity(intent)
            finish()
        }
        val btnLectures: Button = findViewById(R.id.btn_lectures)
        btnLectures.setOnClickListener{
            val intent = Intent(this, lections_display::class.java)
            startActivity(intent)
            finish()
        }
        val btnTests: Button = findViewById(R.id.btn_tests)
        btnTests.setOnClickListener{
            val intent = Intent(this, tests_display::class.java)
            startActivity(intent)
            finish()
        }
        val btnCases: Button = findViewById(R.id.btn_cases)
        btnCases.setOnClickListener{
            val intent = Intent(this, cases_display::class.java)
            startActivity(intent)
            finish()
        }
        val btnGames: Button = findViewById(R.id.btn_games)
        btnGames.setOnClickListener{
            val intent = Intent(this, stud_menu::class.java)
            startActivity(intent)
            finish()
        }
        val btnSchool: ImageView = findViewById(R.id.btn_school)
        btnSchool.setOnClickListener{
            val intent = Intent(this, kur_menu::class.java)
            startActivity(intent)
            finish()
        }
        val btnGroup: ImageView = findViewById(R.id.btn_group)
        btnGroup.setOnClickListener{
            val intent = Intent(this, kur_group_menu::class.java)
            startActivity(intent)
            finish()
        }
        val btnInfo: ImageView = findViewById(R.id.btn_info)
        btnInfo.setOnClickListener{
            val intent = Intent(this, kur_info::class.java)
            startActivity(intent)
            finish()
        }
    }
}