package com.yfujiki.gradeconverter.Views

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.R
import kotlinx.android.synthetic.main.recycler_view_holder.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setGrade(grade: GradeSystem) {
        setGradeName(grade.name)
        setGradeCategory(grade.category)
    }

    fun setIndexes(indexes: List<Int>, grade: GradeSystem) {
        val minIndex = indexes.min() ?: 0
        val maxIndex = indexes.max() ?: 0

        val minGradeValueString = grade.gradeAtIndex(minIndex, false)
        val maxGradeValueString = grade.gradeAtIndex(maxIndex, true)
        val gradeValueString: String?

        if (minGradeValueString == maxGradeValueString) {
            gradeValueString = minGradeValueString
        } else {
            gradeValueString = "$minGradeValueString ~ $maxGradeValueString"
        }

        setGradeValue(gradeValueString)
    }

    private fun setGradeName(gradeName: String) {
        itemView.gradeNameTextView.text = gradeName
    }

    private fun setGradeValue(gradeValue: String) {
        itemView.gradeValueTextView.text = gradeValue
    }

    private fun setGradeCategory(gradeCategory: String) {
        val drawable: Drawable?
        if (gradeCategory.toLowerCase() == "boulder") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = itemView.context.resources.getDrawable(R.drawable.boulder, null)
            } else {
                drawable = itemView.context.resources.getDrawable(R.drawable.boulder)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = itemView.context.resources.getDrawable(R.drawable.sports, null)
            } else {
                drawable = itemView.context.resources.getDrawable(R.drawable.sports)
            }
        }
        itemView.gradeNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}