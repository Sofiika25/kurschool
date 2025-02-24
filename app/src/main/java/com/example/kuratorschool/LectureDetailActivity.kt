package com.example.kuratorschool

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kuratorschool.Models.Lecture
import com.google.firebase.database.*

class LectureDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LectureDetailActivity"
    }

    private lateinit var lectureTitle: TextView
    private lateinit var lectureContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_detail)

        lectureTitle = findViewById(R.id.lectureTitle)
        lectureContent = findViewById(R.id.lectureContent)

        val lectureId = intent.getStringExtra("lectureId")
        if (lectureId.isNullOrEmpty()) {
            Log.e(TAG, "Lecture id is null or empty")
            finish()
            return
        }

        val lectureRef = FirebaseDatabase.getInstance().getReference("Lectures").child(lectureId)
        lectureRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lecture = snapshot.getValue(Lecture::class.java)
                if (lecture != null) {
                    lectureTitle.text = lecture.title
                    lectureContent.text = lecture.content
                } else {
                    Log.e(TAG, "Lecture data is null")
                    Toast.makeText(this@LectureDetailActivity, "Не удалось загрузить лекцию", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Ошибка загрузки лекции: ${error.message}")
                Toast.makeText(this@LectureDetailActivity, "Ошибка загрузки лекции", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
