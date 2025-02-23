package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kuratorschool.Models.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class zayavka_for_school : AppCompatActivity() {
    private val TAG = "ZayavkaForSchool"
    val currentuserId = FirebaseAuth.getInstance().currentUser?.uid

    val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zayavka_for_school)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, school_info::class.java)
            startActivity(intent)
            finish()
        }


        val etFullName = findViewById<EditText>(R.id.usernameEditText)
        val etGroupNumber = findViewById<EditText>(R.id.passwordEditText)
        val etPhone = findViewById<EditText>(R.id.jh)
        val etVkLink = findViewById<EditText>(R.id.pa)
        val etTelegram = findViewById<EditText>(R.id.u)
        val etCuratorReason = findViewById<EditText>(R.id.p)
        val etPositiveQualities = findViewById<EditText>(R.id.pp)
        val etNegativeQualities = findViewById<EditText>(R.id.ppp)
        val etStudentExperience = findViewById<EditText>(R.id.pppq)

        val btnSubmit: Button = findViewById(R.id.loginButton)
        btnSubmit.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val groupNumber = etGroupNumber.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val vkLink = etVkLink.text.toString().trim()
            val telegram = etTelegram.text.toString().trim()
            val curatorReason = etCuratorReason.text.toString().trim()
            val positiveQualities = etPositiveQualities.text.toString().trim()
            val negativeQualities = etNegativeQualities.text.toString().trim()
            val studentExperience = etStudentExperience.text.toString().trim()

            if (fullName.isEmpty() || groupNumber.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val application = Application(
                fullName = fullName,
                groupNumber = groupNumber,
                phone = phone,
                vkLink = vkLink,
                telegram = telegram,
                curatorReason = curatorReason,
                positiveQualities = positiveQualities,
                negativeQualities = negativeQualities,
                studentExperience = studentExperience,
                status = "на рассмотрении",
                submissionDate = currentDate,
                user_id=currentuserId
            )

            val dbRef = FirebaseDatabase.getInstance().getReference("Applications")
            val newApplicationRef = dbRef.push()
            newApplicationRef.setValue(application).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Заявка отправлена. Статус: ${application.status}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Заявка успешно сохранена с ключом: ${newApplicationRef.key}")
                    val intent = Intent(this, school_info::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Ошибка отправки заявки: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Ошибка при сохранении заявки", task.exception)
                }
            }
        }
    }
}
