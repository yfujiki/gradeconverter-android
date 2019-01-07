package com.responsivebytes.gradeconverter.Views

import android.support.v7.widget.RecyclerView
import android.view.View
import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.GradeSystem
import kotlinx.android.synthetic.main.add_recycler_view_holder.view.*

class AddRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setGrade(grade: GradeSystem) {
        val context = GCApp.getInstance().applicationContext
        setGradeName(grade.localizedName(context))
        setGradeCategory(grade.localizedCategory(context))
    }

    private fun setGradeName(gradeName: String) {
        itemView.gradeNameTextView.text = gradeName
    }

    private fun setGradeCategory(gradeCategory: String) {
        itemView.gradeCategoryTextView.text = "($gradeCategory)"
    }
}