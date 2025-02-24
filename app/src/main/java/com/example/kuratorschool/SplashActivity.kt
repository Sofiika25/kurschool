package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kuratorschool.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp
class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            Log.d(TAG, "Пользователь найден: ${currentUser.uid}")
            checkUserRole(currentUser.uid)
        } else {
            Log.d(TAG, "Пользователь не авторизован, открываем Authorization")
            startActivity(Intent(this, Authorization::class.java))
            finish()
        }
    }

    private fun checkUserRole(uid: String) {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        usersRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    Log.d(TAG, "Роль пользователя: ${user.role}")
                    when (user.role) {
                        "студент" -> {
                            startActivity(Intent(this, stud_menu::class.java))
                        }
                        "старший_куратор" -> {
                            startActivity(Intent(this, kur_menu::class.java))
                        }
                        else -> {
                            Log.d(TAG, "Неизвестная роль, отправляем на авторизацию")
                            startActivity(Intent(this, Authorization::class.java))
                        }
                    }
                }
            } else {
                Log.d(TAG, "Пользователь не найден в базе, открываем Authorization")
                startActivity(Intent(this, Authorization::class.java))
            }
            finish()
        }.addOnFailureListener { e ->
            Log.e(TAG, "Ошибка получения данных: ${e.message}")
            startActivity(Intent(this, Authorization::class.java))
            finish()
        }
    }
}
