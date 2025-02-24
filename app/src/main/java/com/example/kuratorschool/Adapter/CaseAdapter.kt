package com.example.kuratorschool

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Case

class CasesAdapter(
    private val casesList: List<Case>,
    private val activityLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {

    inner class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val CaseTitle: TextView = itemView.findViewById(R.id.caseTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_case, parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val currentCase = casesList[position]
        holder.CaseTitle.text = currentCase.title

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, case1::class.java)
            intent.putExtra("CASE_ID", currentCase.id)
            activityLauncher.launch(intent)
        }
    }

    override fun getItemCount(): Int = casesList.size
}
