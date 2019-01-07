package com.responsivebytes.gradeconverter.Views

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.responsivebytes.gradeconverter.Models.GradeSystem
import kotlinx.android.synthetic.main.add_recycler_view_holder.view.*

class AddRecyclerViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    fun setGrade(grade: GradeSystem) {
        setGradeName(grade.name)
        setGradeCategory(grade.category)
    }

    private fun setGradeName(gradeName: String) {
        itemView.gradeNameTextView.text = gradeName
    }

    private fun setGradeCategory(gradeCategory: String) {
        itemView.gradeCategoryTextView.text = "($gradeCategory)"
    }
}