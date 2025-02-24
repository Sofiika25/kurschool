package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Lecture
import com.google.firebase.database.*

class lections_display : AppCompatActivity() {

    companion object {
        private const val TAG = "lections_display"
    }

    private lateinit var lecturesRecyclerView: RecyclerView
    private lateinit var lecturesList: MutableList<Lecture>
    private lateinit var lecturesAdapter: LecturesAdapter
    private val lecturesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Lectures")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lections_display)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, kur_menu::class.java)
            startActivity(intent)
            finish()
        }

        val btnSchool: ImageView = findViewById(R.id.btn_school)
        btnSchool.setOnClickListener {
            val intent = Intent(this, kur_menu::class.java)
            startActivity(intent)
            finish()
        }
        val btnGroup: ImageView = findViewById(R.id.btn_group)
        btnGroup.setOnClickListener {
            val intent = Intent(this, kur_group_menu::class.java)
            startActivity(intent)
            finish()
        }
        val btnInfo: ImageView = findViewById(R.id.btn_info)
        btnInfo.setOnClickListener {
            val intent = Intent(this, kur_info::class.java)
            startActivity(intent)
            finish()
        }

        lecturesRecyclerView = findViewById(R.id.lecturesRecyclerView)
        lecturesRecyclerView.layoutManager = LinearLayoutManager(this)
        lecturesList = mutableListOf()
        lecturesAdapter = LecturesAdapter(lecturesList) { lecture ->
            Log.d(TAG, "Выбрана лекция: ${lecture.id}, isAccessible: ${lecture.isAccessible}")

            if (lecture.isAccessible) {
                val intent = Intent(this, LectureDetailActivity::class.java)
                intent.putExtra("lectureId", lecture.id)
                Log.d(TAG, "Открытие лекции: ${lecture.id}")
                startActivity(intent)
            } else {
                Log.w(TAG, "Лекция ${lecture.id} пока недоступна")
                Toast.makeText(this, "Лекция пока недоступна", Toast.LENGTH_SHORT).show()
            }
        }
        lecturesRecyclerView.adapter = lecturesAdapter
        loadLectures()
    }

    private fun loadLectures() {
        Log.d(TAG, "Загрузка лекций из Firebase...")
        lecturesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lecturesList.clear()
                Log.d(TAG, "Получено ${snapshot.childrenCount} лекций")
                for (child in snapshot.children) {
                    val lecture = child.getValue(Lecture::class.java)
                    lecture?.isAccessible = child.child("isAccessible").getValue(Boolean::class.java) ?: false
                    if (lecture != null) {
                        Log.d(TAG, "Лекция загружена: id=${lecture.id}, title=${lecture.title}, isAccessible=${lecture.isAccessible}")
                        lecturesList.add(lecture)
                    } else {
                        Log.w(TAG, "Ошибка парсинга лекции: $child")
                    }
                }
                lecturesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Ошибка загрузки лекций: ${error.message}")
            }
        })
    }
}
