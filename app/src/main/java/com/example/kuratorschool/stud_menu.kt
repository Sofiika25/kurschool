package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class stud_menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stud_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val infolay: LinearLayout = findViewById(R.id.info_lay)
        infolay.setOnClickListener{
            val intent = Intent(this, info_display::class.java)
            startActivity(intent)
            finish()
        }
        val schoollay: LinearLayout = findViewById(R.id.school_lay)
        schoollay.setOnClickListener{
            val intent = Intent(this, school_info::class.java)
            startActivity(intent)
            finish()
        }
        val btnBack: TextView = findViewById(R.id.btn_exit)
        btnBack.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Authorization::class.java)
            startActivity(intent)
            finish()
        }
    }
}