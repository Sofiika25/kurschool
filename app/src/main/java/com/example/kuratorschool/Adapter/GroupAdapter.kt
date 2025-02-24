package com.example.kuratorschool.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Group
import com.example.kuratorschool.R

class GroupAdapter(private val groups: List<Group>) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupTitle: TextView = itemView.findViewById(R.id.group_title)
        val studentsRecycler: RecyclerView = itemView.findViewById(R.id.students_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.groupTitle.text = "Группа ${group.group_id}"

        holder.studentsRecycler.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.studentsRecycler.adapter = StudentAdapter(group.students)
    }

    override fun getItemCount(): Int = groups.size
}
