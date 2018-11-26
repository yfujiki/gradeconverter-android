package com.yfujiki.gradeconverter.Views

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.R
import kotlinx.android.synthetic.main.recycler_view_holder.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun setGrade(grade: GradeSystem) {
        itemView.gradeNameTextView.text = grade.name
        val drawable: Drawable?
        if (grade.category.toLowerCase() == "boulder") {
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

    fun setIndexes(indexes: List<Int>, grade: GradeSystem) {
        val minIndex = indexes.min() ?: 0
        val maxIndex = indexes.max() ?: 0

        val gradeValueString: String?
        if (minIndex == maxIndex) {
            gradeValueString = grade.gradeAtIndex(minIndex, false)
        } else {
            val minGradeValueString = grade.gradeAtIndex(minIndex, false)
            val maxGradeValueString = grade.gradeAtIndex(maxIndex, true)
            gradeValueString = "$minGradeValueString ~ $maxGradeValueString"
        }

        itemView.gradeValueTextView.text = gradeValueString
    }
    fun setGradeName(gradeName: String) {
        itemView.gradeNameTextView.text = gradeName
    }

    fun setGradeValue(gradeValue: String) {
        itemView.gradeValueTextView.text = gradeValue
    }
}