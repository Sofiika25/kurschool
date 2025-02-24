package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kuratorschool.Models.Case
import com.example.kuratorschool.Models.UserAnswer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class case1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_case1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        val caseId = intent.getStringExtra("CASE_ID") ?: "case1"
        val tvTitle: TextView = findViewById(R.id.testTitle)
        val tvQuestion: TextView = findViewById(R.id.questionLabel)
        val etAnswer: EditText = findViewById(R.id.otvet)
        val btnSend: Button = findViewById(R.id.enterButton)

        val caseRef = FirebaseDatabase.getInstance().getReference("Cases").child(caseId)
        caseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentCase = snapshot.getValue(Case::class.java)
                if (currentCase != null) {
                    tvTitle.text = currentCase.title
                    tvQuestion.text = currentCase.question
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        btnSend.setOnClickListener {
            val userAnswerText = etAnswer.text.toString().trim()
            if (userAnswerText.isNotEmpty()) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val answer = UserAnswer(
                        answer = userAnswerText,
                        timestamp = getCurrentDateTime(),
                    )
                    caseRef.child("userAnswers").child(currentUser.uid)
                        .setValue(answer)
                        .addOnSuccessListener {
                            Toast.makeText(this@case1, "Ответ успешно отправлен!", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@case1, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this@case1, "Введите ответ перед отправкой", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }
}
