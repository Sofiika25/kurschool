package com.example.kuratorschool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Lecture

class LecturesAdapter(
    private val lectures: List<Lecture>,
    private val onItemClick: (Lecture) -> Unit
) : RecyclerView.Adapter<LecturesAdapter.LectureViewHolder>() {

    inner class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lectureTitle: TextView = itemView.findViewById(R.id.lectureTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lecture, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.lectureTitle.text = lecture.title

        holder.lectureTitle.alpha = if (lecture.isAccessible) 1.0f else 0.5f

        holder.itemView.setOnClickListener {
            onItemClick(lecture)
        }
    }

    override fun getItemCount(): Int = lectures.size
}
