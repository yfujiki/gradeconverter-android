package com.yfujiki.gradeconverter.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yfujiki.gradeconverter.MainActivity
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.R
import com.yfujiki.gradeconverter.Views.AddRecyclerViewHolder

class AddRecyclerViewAdapter(val activity: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.add_recycler_view_holder, p0, false)
        val viewHolder = AddRecyclerViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        val totalCount = GradeSystemTable.tableSize
        val selectedCount = LocalPreferences.selectedGradeSystems().size
        return totalCount - selectedCount
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as AddRecyclerViewHolder
        val marginParams = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if(p1 == 0) {
            marginParams.topMargin = 32
            marginParams.bottomMargin = 16
        } else if (p1 == itemCount - 1) {
            marginParams.topMargin = 16
            marginParams.bottomMargin = 32
        } else {
            marginParams.topMargin = 16
            marginParams.bottomMargin = 16
        }
        viewHolder.itemView.layoutParams = marginParams
        viewHolder.itemView.requestLayout()
        val gradeSystem = GradeSystemTable.gradeSystemsToAdd()[p1]
        viewHolder.setGrade(gradeSystem)

        viewHolder.itemView.setOnClickListener({
            val position = GradeSystemTable.gradeSystemsToAdd().indexOf(gradeSystem)
            LocalPreferences.addSelectedGradeSystem(gradeSystem)
            notifyItemRemoved(position)
        })
    }
}
