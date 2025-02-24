package com.example.kuratorschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Adapter.GroupAdapter
import com.example.kuratorschool.Models.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class my_group_display : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter
    private val groupList = mutableListOf<Group>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_group_display)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack: TextView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, kur_group_menu::class.java))
            finish()
        }

        recyclerView = findViewById(R.id.recycler_view_groups)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GroupAdapter(groupList)
        recyclerView.adapter = adapter

        loadGroupsForCurator()
    }

    private fun loadGroupsForCurator() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("Groups")

        ref.orderByChild("curator_id").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val updatedGroupList = mutableListOf<Group>()
                    for (child in snapshot.children) {
                        child.getValue(Group::class.java)?.let { updatedGroupList.add(it) }
                    }
                    groupList.clear()
                    groupList.addAll(updatedGroupList)
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Ошибка загрузки групп: ${error.message}")
                }
            })
    }

}
