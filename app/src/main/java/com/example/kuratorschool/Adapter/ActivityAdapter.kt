package com.example.kuratorschool.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Activity
import com.example.kuratorschool.R
import com.example.kuratorschool.activnost_detail

class ActivityAdapter(private val context: Context, private val activities: List<Activity>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val activityName: TextView = itemView.findViewById(R.id.activity_name)
        val activityDate: TextView = itemView.findViewById(R.id.activity_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.activityName.text = activity.nameActivity
        holder.activityDate.text = "Дата: ${activity.date}"


        holder.itemView.setOnClickListener {
            val intent = Intent(context, activnost_detail::class.java).apply {
                putExtra("activity_name", activity.nameActivity)
                putExtra("activity_date", activity.date)
                putStringArrayListExtra("participants", ArrayList(activity.participants.map { it.fullName }))
            }
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            showActivityDialog(activity)
            true
        }
    }

    override fun getItemCount(): Int = activities.size

    private fun showActivityDialog(activity: Activity) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_activity_details, null)

        val titleTextView: TextView = dialogView.findViewById(R.id.dialog_title)
        val dateTextView: TextView = dialogView.findViewById(R.id.dialog_date)
        val participantsTextView: TextView = dialogView.findViewById(R.id.dialog_participants)

        titleTextView.text = activity.nameActivity
        dateTextView.text = "Дата: ${activity.date}"
        participantsTextView.text = activity.participants.joinToString("\n") { it.fullName }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("ОК", null)
            .create()
            .show()
    }

}
