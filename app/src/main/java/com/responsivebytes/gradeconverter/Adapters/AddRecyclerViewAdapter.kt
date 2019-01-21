package com.responsivebytes.gradeconverter.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.GradeSystemTable
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.R
import com.responsivebytes.gradeconverter.Utilities.Screen
import com.responsivebytes.gradeconverter.Views.AddRecyclerViewHolder

class AddRecyclerViewAdapter(val localPreferences: LocalPreferences) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.add_recycler_view_holder, p0, false)
        val viewHolder = AddRecyclerViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        val totalCount = GradeSystemTable.tableSize
        val selectedCount = localPreferences.selectedGradeSystems().size
        return totalCount - selectedCount
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as AddRecyclerViewHolder

        setInterItemSpace(viewHolder, p1)

        viewHolder.itemView.requestLayout()
        val gradeSystem = localPreferences.unselectedGradeSystems()[p1]
        viewHolder.setGrade(gradeSystem)

        viewHolder.itemView.setOnClickListener({
            val position = localPreferences.unselectedGradeSystems().indexOf(gradeSystem)
            localPreferences.addSelectedGradeSystem(gradeSystem)
            notifyItemRemoved(position)
        })
    }

    private fun setInterItemSpace(viewHolder: RecyclerView.ViewHolder, index: Int) {
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
