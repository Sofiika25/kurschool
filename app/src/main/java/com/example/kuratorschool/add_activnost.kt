package com.example.kuratorschool

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class add_activnost : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var groupsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activnost)
        val btnBack: TextView = findViewById(R.id.btn_back)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("ActivityTracking")
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups")

        val activityNameInput: EditText = findViewById(R.id.answerInput)
        val activityDateInput: EditText = findViewById(R.id.an)
        val participantNameInput: EditText = findViewById(R.id.answerI)
        val addButton: Button = findViewById(R.id.addButton)


        btnBack.setOnClickListener {
            finish()
        }

        addButton.setOnClickListener {
            val activityName = activityNameInput.text.toString().trim()
            val activityDate = activityDateInput.text.toString().trim()
            val participantName = participantNameInput.text.toString().trim()
            val curatorId = auth.currentUser?.uid ?: "unknown"

            if (activityName.isEmpty() || activityDate.isEmpty() || participantName.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            getGroupIdForCurator(curatorId) { groupId ->
                val activityId = database.push().key ?: return@getGroupIdForCurator
                val activity = hashMapOf(
                    "activity_id" to activityId,
                    "curator_id" to curatorId,
                    "date" to activityDate,
                    "group_id" to groupId,
                    "name_activity" to activityName,
                    "participants" to listOf(
                        mapOf("full_name" to participantName, "user_id" to database.push().key)
                    )
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
