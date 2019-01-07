package com.responsivebytes.gradeconverter.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.R
import com.responsivebytes.gradeconverter.Utilities.Screen
import com.responsivebytes.gradeconverter.Views.AddRecyclerViewHolder

class AddRecyclerViewAdapter() : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.add_recycler_view_holder, p0, false)
        val viewHolder = AddRecyclerViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        val totalCount = GradeSystemTable.tableSize
        val selectedCount = LocalPreferences.selectedGradeSystems().size
        return totalCount - selectedCount
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as AddRecyclerViewHolder

        setInterItemSpace(viewHolder, p1)

        viewHolder.itemView.requestLayout()
        val gradeSystem = GradeSystemTable.gradeSystemsToAdd()[p1]
        viewHolder.setGrade(gradeSystem)

        viewHolder.itemView.setOnClickListener({
            val position = GradeSystemTable.gradeSystemsToAdd().indexOf(gradeSystem)
            LocalPreferences.addSelectedGradeSystem(gradeSystem)
            notifyItemRemoved(position)
        })
    }

    private fun setInterItemSpace(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, index: Int) {
        val marginParams = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        val context = GCApp.getInstance().applicationContext

        if (index == 0) {
            marginParams.topMargin = if (Screen.isSparseScreen(context)) 24 else 32
            marginParams.bottomMargin = if (Screen.isSparseScreen(context)) 12 else 16
        } else if (index == itemCount - 1) {
            marginParams.topMargin = if (Screen.isSparseScreen(context)) 12 else 16
            marginParams.bottomMargin = if (Screen.isSparseScreen(context)) 24 else 32
        } else {
            marginParams.topMargin = if (Screen.isSparseScreen(context)) 12 else 16
            marginParams.bottomMargin = if (Screen.isSparseScreen(context)) 12 else 16
        }
        viewHolder.itemView.layoutParams = marginParams
    }
}
