package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class school_info : AppCompatActivity() {

    companion object {
        private const val TAG = "school_info"
    }

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
        btnBack.setOnClickListener {
            val intent = Intent(this, stud_menu::class.java)
            startActivity(intent)
            finish()
        }

        val zayavkaBtn: Button = findViewById(R.id.zayavkaButton)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        zayavkaBtn.setOnClickListener {
            if (userId == null) {
                Log.e(TAG, "Ошибка: пользователь не авторизован")
                Toast.makeText(this, "Ошибка: пользователь не авторизован", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Проверяем заявку пользователя с ID: $userId")
            val dbRef = FirebaseDatabase.getInstance().getReference("Applications")
            dbRef.orderByChild("user_id").equalTo(userId).get()
                .addOnSuccessListener { snapshot ->
                    var hasApplicationUnderReview = false
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val status = child.child("status").getValue(String::class.java)
                            Log.d(TAG, "Найдена заявка с ID ${child.key}, статус: $status")
                            if (status == "на рассмотрении") {
                                hasApplicationUnderReview = true
                                break
                            }
                        }
                    }
                    if (hasApplicationUnderReview) {
                        Log.w(TAG, "Заявка уже на рассмотрении, повторная отправка невозможна")
                        Toast.makeText(this, "Вы уже отправили заявку, она на рассмотрении", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "Открываем экран подачи заявки")
                        val intent = Intent(this, zayavka_for_school::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Ошибка при проверке заявки", exception)
                    Toast.makeText(this, "Ошибка проверки заявки", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
