package com.yfujiki.gradeconverter.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yfujiki.gradeconverter.MainActivity
import com.yfujiki.gradeconverter.Models.AppState
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.R
import com.yfujiki.gradeconverter.Utilities.Screen
import com.yfujiki.gradeconverter.Views.MainRecyclerViewHolder
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewAdapter(val activity: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        setInterItemSpace(viewHolder, p1)

        when (AppState.mainViewMode) {
            AppState.MainViewMode.normal -> {
                viewHolder.itemView.deleteButton.visibility = View.GONE
                viewHolder.itemView.handle.visibility = View.GONE
                viewHolder.itemView.leftArrow.visibility = View.VISIBLE
                viewHolder.itemView.rightArrow.visibility = View.VISIBLE

                viewHolder.itemView.rightArrow.setOnClickListener {
                    swipeLeft(p0, p1)
                }

                viewHolder.itemView.leftArrow.setOnClickListener {
                    swipeRight(p0, p1)
                }
            }
            AppState.MainViewMode.edit -> {
                viewHolder.itemView.leftArrow.visibility = View.GONE
                viewHolder.itemView.rightArrow.visibility = View.GONE
                viewHolder.itemView.deleteButton.visibility = View.VISIBLE
                viewHolder.itemView.handle.visibility = View.VISIBLE

                viewHolder.itemView.deleteButton.setOnClickListener {
                    deleteItemAt(p1)
                }
            }
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

    private fun deleteItemAt(index: Int) {
        val gradeSystemToDelete = LocalPreferences.selectedGradeSystems()[index]
        LocalPreferences.removeSelectedGradeSystem(gradeSystemToDelete)

        notifyItemRemoved(index)
    }

    private fun setInterItemSpace(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val marginParams = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (index == 0) {
            marginParams.topMargin = if (Screen.isSparseScreen(activity)) 8 else 16
            marginParams.bottomMargin = if (Screen.isSparseScreen(activity)) 6 else 8
        } else if (index == itemCount - 1) {
            marginParams.topMargin = if (Screen.isSparseScreen(activity)) 6 else 8
            marginParams.bottomMargin = if (Screen.isSparseScreen(activity)) 8 else 16
        } else {
            marginParams.topMargin = if (Screen.isSparseScreen(activity)) 6 else 8
            marginParams.bottomMargin = if (Screen.isSparseScreen(activity)) 6 else 8
        }
        viewHolder.itemView.layoutParams = marginParams
    }
}
