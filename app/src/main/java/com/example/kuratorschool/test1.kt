package com.example.kuratorschool

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kuratorschool.Models.QuestionViewHolder
import com.example.kuratorschool.Models.Test
import com.example.kuratorschool.Models.TestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener




class test1 : AppCompatActivity() {

    private val questionViews = mutableMapOf<String, QuestionViewHolder>()
    private lateinit var test: Test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, tests_display::class.java))
            finish()
        }

        val testId = intent.getStringExtra("testId")
        if (testId == null) {
            Toast.makeText(this, "Тест не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val ref = FirebaseDatabase.getInstance().getReference("Tests").child(testId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                test = snapshot.getValue(Test::class.java) ?: return
                findViewById<TextView>(R.id.testTitle).text = test.title
                generateQuestionsUI()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@test1, "Ошибка загрузки теста", Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            evaluateTest()
        }
    }

    private fun generateQuestionsUI() {
        val container = findViewById<LinearLayout>(R.id.questionsContainer)
        container.removeAllViews()
        val inflater: LayoutInflater = LayoutInflater.from(this)

        for ((questionId, question) in test.questions) {
            val questionItemView = inflater.inflate(R.layout.question_item, container, false)
            val questionText = questionItemView.findViewById<TextView>(R.id.questionText)
            val answerContainer = questionItemView.findViewById<FrameLayout>(R.id.answerContainer)

            questionText.text = question.question

            when (question.type) {
                "text" -> {
                    val editText = EditText(this).apply {
                        hint = "Ваш ответ"
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        setPadding(8, 8, 8, 8)
                    }
                    answerContainer.addView(editText)
                    questionViews[questionId] = QuestionViewHolder("text", editText)
                }
                "single_choice" -> {
                    val radioGroup = RadioGroup(this)
                    question.options?.forEach { (optionKey, optionValue) ->
                        val radioButton = RadioButton(this).apply {
                            text = optionValue
                            tag = optionKey
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        }
                        radioGroup.addView(radioButton)
                    }
                    answerContainer.addView(radioGroup)
                    questionViews[questionId] = QuestionViewHolder("single_choice", radioGroup)
                }
                "multiple_choice" -> {
                    val linearLayout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
                    question.options?.forEach { (optionKey, optionValue) ->
                        val checkBox = CheckBox(this).apply {
                            text = optionValue
                            tag = optionKey
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        }
                        linearLayout.addView(checkBox)
                    }
                    answerContainer.addView(linearLayout)
                    questionViews[questionId] = QuestionViewHolder("multiple_choice", linearLayout)
                }
                else -> {
                    Toast.makeText(this, "Неподдерживаемый тип вопроса", Toast.LENGTH_SHORT).show()
                }
            }

            container.addView(questionItemView)
        }
    }

    private fun evaluateTest() {
        var score = 0
        val total = test.questions.size

        for ((questionId, question) in test.questions) {
            val holder = questionViews[questionId] ?: continue
            when (holder.type) {
                "text" -> {
                    val answer = (holder.view as EditText).text.toString().trim()
                    if (answer.equals(question.correctAnswer, ignoreCase = true))
                        score++
                }
                "single_choice" -> {
                    val radioGroup = holder.view as RadioGroup
                    val selectedId = radioGroup.checkedRadioButtonId
                    if (selectedId != -1) {
                        val radioButton = radioGroup.findViewById<RadioButton>(selectedId)
                        val answer = radioButton.tag.toString()
                        if (answer == question.correctAnswer)
                            score++
                    }
                }
                "multiple_choice" -> {
                    val layout = holder.view as LinearLayout
                    val userAnswers = mutableListOf<String>()
                    for (i in 0 until layout.childCount) {
                        val child = layout.getChildAt(i)
                        if (child is CheckBox && child.isChecked)
                            userAnswers.add(child.tag.toString())
                    }
                    val correctAnswers = question.correctAnswers ?: emptyList()
                    if (userAnswers.sorted() == correctAnswers.sorted())
                        score++
                }
            }
        }

        Toast.makeText(this, "Результат: $score из $total", Toast.LENGTH_LONG).show()


        saveResult(score, total)
    }

    private fun saveResult(score: Int, total: Int) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
        val result = TestResult(testId = test.id, userId = userId, score = score, total = total)
        FirebaseDatabase.getInstance().getReference("Results").push().setValue(result)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Результаты сохранены", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, tests_display::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Ошибка сохранения результатов", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
