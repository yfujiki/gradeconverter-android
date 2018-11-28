package com.yfujiki.gradeconverter.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.R
import com.yfujiki.gradeconverter.Views.MainRecyclerViewHolder
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_holder, p0, false)
        return MainRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return LocalPreferences.selectedGradeSystems().size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as MainRecyclerViewHolder
        val grade = LocalPreferences.selectedGradeSystems()[p1]
        viewHolder.setGrade(grade)
        viewHolder.setIndexes(LocalPreferences.currentIndexes(), grade)

        viewHolder.itemView.rightArrow.setOnClickListener {
            swipeLeft(p0, p1)
        }

        viewHolder.itemView.leftArrow.setOnClickListener {
            swipeRight(p0, p1)
        }
    }

    fun swipeLeft(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val gradeSystem = LocalPreferences.selectedGradeSystems()[position]
        val currentIndexes = LocalPreferences.currentIndexes()
        val nextGrade = gradeSystem.higherGradeFromIndexes(currentIndexes)
        moveToNextGrade(nextGrade, gradeSystem)
    }

    fun swipeRight(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val gradeSystem = LocalPreferences.selectedGradeSystems()[position]
        val currentIndexes = LocalPreferences.currentIndexes()
        val nextGrade = gradeSystem.lowerGradeFromIndexes(currentIndexes)
        moveToNextGrade(nextGrade, gradeSystem)
    }

    private fun moveToNextGrade(nextGrade: String?, gradeSystem: GradeSystem) {
        if (nextGrade != null) {
            val nextIndexes = gradeSystem.indexesForGrade(nextGrade)
            LocalPreferences.setCurrentIndexes(nextIndexes)
        }

        notifyDataSetChanged()
    }
}
