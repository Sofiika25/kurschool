package com.example.kuratorschool.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuratorschool.Models.Test
import com.example.kuratorschool.Models.TestResult
import com.example.kuratorschool.R

class TestsAdapter(
    private var tests: List<Test>,
    private var resultsMap: Map<String, TestResult>,
    private val onItemClick: (Test) -> Unit
) : RecyclerView.Adapter<TestsAdapter.TestViewHolder>() {

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testTitle: TextView = itemView.findViewById(R.id.test_title)
        val testResult: TextView = itemView.findViewById(R.id.test_result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = tests[position]
        holder.testTitle.text = test.title

        if (resultsMap.containsKey(test.id)) {
            val result = resultsMap[test.id]
            holder.testResult.visibility = View.VISIBLE
            holder.testResult.text = "Результат: ${result?.score} из ${result?.total}"
            holder.itemView.setOnClickListener(null)
            holder.itemView.isEnabled = false
            holder.itemView.alpha = 0.5f
        } else {
            holder.testResult.visibility = View.GONE
            holder.itemView.isEnabled = true
            holder.itemView.alpha = 1.0f
            holder.itemView.setOnClickListener { onItemClick(test) }
        }
    }

    override fun getItemCount() = tests.size

    fun updateList(newTests: List<Test>) {
        tests = newTests
        notifyDataSetChanged()
    }

    fun updateResults(newResults: Map<String, TestResult>) {
        resultsMap = newResults
        notifyDataSetChanged()
    }
}
