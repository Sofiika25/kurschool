package com.example.kuratorschool

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kuratorschool.Models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Locale

class add_activnost : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var groupsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var currentGroupId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activnost)

        val btnBack: TextView = findViewById(R.id.btn_back)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("ActivityTracking")
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups")

        val activityNameInput: EditText = findViewById(R.id.answerInput)
        val activityDateInput: EditText = findViewById(R.id.an)
        val studentsListView: ListView = findViewById(R.id.studentsListView)
        val addButton: Button = findViewById(R.id.addButton)

        btnBack.setOnClickListener {
            finish()
        }

        val curatorId = auth.currentUser?.uid ?: "unknown"


        getGroupIdForCurator(curatorId) { groupId ->
            currentGroupId = groupId
            if (groupId != 0) {
                groupsRef.child(groupId.toString()).child("students")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val studentsList = mutableListOf<Student>()
                            for (child in snapshot.children) {
                                val student = child.getValue(Student::class.java)
                                if (student != null) {
                                    studentsList.add(student)
                                }
                            }
                            if (studentsList.isEmpty()) {
                                Toast.makeText(
                                    this@add_activnost,
                                    "В группе нет студентов для выбора",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val adapter = ArrayAdapter(
                                    this@add_activnost,
                                    android.R.layout.simple_list_item_multiple_choice,
                                    studentsList.map { it.full_name }
                                )
                                studentsListView.adapter = adapter
                                studentsListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
                                studentsListView.tag = studentsList
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@add_activnost,
                                "Ошибка загрузки студентов",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Группа не найдена", Toast.LENGTH_SHORT).show()
            }
        }

        addButton.setOnClickListener {
            val activityName = activityNameInput.text.toString().trim()
            val activityDate = activityDateInput.text.toString().trim()

            // Получаем список студентов из tag ListView
            val studentsList = studentsListView.tag as? List<Student>

            if (activityName.isEmpty() || activityDate.isEmpty() || studentsList.isNullOrEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Получаем позиции выбранных элементов
            val checkedPositions = studentsListView.checkedItemPositions
            val selectedStudents = mutableListOf<Student>()
            for (i in 0 until checkedPositions.size()) {
                val position = checkedPositions.keyAt(i)
                if (checkedPositions.valueAt(i)) {
                    selectedStudents.add(studentsList[position])
                }
            }

            if (selectedStudents.isEmpty()) {
                Toast.makeText(this, "Выберите хотя бы одного студента", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Проверка корректности введённой даты
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            try {
                dateFormat.parse(activityDate)
            } catch (e: Exception) {
                Toast.makeText(this, "Введите корректную дату в формате dd.MM.yyyy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val activityId = database.push().key ?: return@setOnClickListener

            // Формируем список участников в виде списка map
            val participants = selectedStudents.map { student ->
                mapOf("full_name" to student.full_name, "user_id" to student.user_id)
            }

            val activity = hashMapOf(
                "activity_id" to activityId,
                "curator_id" to curatorId,
                "date" to activityDate,
                "group_id" to currentGroupId,
                "name_activity" to activityName,
                "participants" to participants
            )

            database.child(activityId).setValue(activity)
                .addOnSuccessListener {
                    Toast.makeText(this, "Активность добавлена", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show()
                }
        }

    }


    private fun getGroupIdForCurator(curatorId: String, callback: (Int) -> Unit) {
        groupsRef.orderByChild("curator_id").equalTo(curatorId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val groupId = child.child("group_id").getValue(Int::class.java) ?: 0
                        callback(groupId)
                        return
                    }
                    callback(0)
                }
                override fun onCancelled(error: DatabaseError) {
                    callback(0)
                }
            })
    }
}
