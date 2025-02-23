package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kuratorschool.Models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Authorization : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private val TAG = "Authorization"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_authorization)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null) {
            Log.d(TAG, "Пользователь уже авторизован, проверка роли: ${auth!!.currentUser!!.uid}")
            checkUserRole(auth!!.currentUser!!.uid)
            return
        }
        val loginEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener { v: View? ->
            val loginText = loginEditText.text.toString().trim { it <= ' ' }
            val passwordText = passwordEditText.text.toString().trim { it <= ' ' }

            if (loginText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Пустой логин или пароль")
                return@setOnClickListener
            }

            Log.d(TAG, "Попытка авторизации с email: $loginText")
            auth!!.signInWithEmailAndPassword(loginText, passwordText)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        val currentUser = auth!!.currentUser
                        Log.d(TAG, "Авторизация успешна, currentUser: $currentUser")
                        if (currentUser != null) {
                            checkUserRole(currentUser.uid)
                        } else {
                            Log.e(TAG, "currentUser равен null после успешной авторизации")
                        }
                    } else {
                        Log.e(TAG, "Ошибка авторизации: ${task.exception?.message}")
                        Toast.makeText(
                            this,
                            "Ошибка авторизации: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun checkUserRole(uid: String) {
        Log.d(TAG, "Проверка роли пользователя для uid: $uid")
        val usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        Log.d(TAG, "Обращаемся к узлу: ${usersRef.path}")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "Получен snapshot: $snapshot")
                if (snapshot.exists() && snapshot.value != null) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        Log.d(TAG, "Данные пользователя: $user")
                        when (user.role) {
                            "студент" -> {
                                Log.d(TAG, "Роль: студент, открытие stud_menu")
                                startActivity(Intent(this@Authorization, stud_menu::class.java))
                                finish()
                            }
                            "старший_куратор" -> {
                                Log.d(TAG, "Роль: старший_куратор, открытие kur_menu")
                                startActivity(Intent(this@Authorization, kur_menu::class.java))
                                finish()
                            }
                            else -> {
                                Log.d(TAG, "Неизвестная роль: ${user.role}")
                                Toast.makeText(
                                    this@Authorization,
                                    "Неизвестная роль",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Log.e(TAG, "Полученные данные пользователя пусты")
                        Toast.makeText(
                            this@Authorization,
                            "Ошибка: данные пользователя пусты",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.d(TAG, "Snapshot пользователя не существует")
                    Toast.makeText(
                        this@Authorization,
                        "Пользователь не найден",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Ошибка базы данных: ${error.message}")
                Toast.makeText(
                    this@Authorization,
                    "Ошибка базы данных: " + error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
