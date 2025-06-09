package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class kur_info : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kur_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnExit: TextView = findViewById(R.id.btn_exit)
        btnExit.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Authorization::class.java)
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
        val stip: TextView = findViewById(R.id.stip)
        stip.setOnClickListener {
            val intent = Intent(this, info_stipendiya::class.java)
            startActivity(intent)
        }
    }
}