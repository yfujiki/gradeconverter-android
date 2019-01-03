package com.yfujiki.gradeconverter.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.yfujiki.gradeconverter.GCApp
import com.yfujiki.gradeconverter.Models.AppState
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.R
import com.yfujiki.gradeconverter.Utilities.Screen
import com.yfujiki.gradeconverter.Views.MainRecyclerViewHolder
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val animation by lazy {
        AnimationUtils.loadAnimation(GCApp.getInstance().applicationContext, R.anim.wobble)
    }

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

                viewHolder.itemView.clearAnimation()
            }
            AppState.MainViewMode.edit -> {
                viewHolder.itemView.leftArrow.visibility = View.GONE
                viewHolder.itemView.rightArrow.visibility = View.GONE
                viewHolder.itemView.deleteButton.visibility = View.VISIBLE
                viewHolder.itemView.handle.visibility = View.VISIBLE

                viewHolder.itemView.deleteButton.setOnClickListener {
                    deleteItemAt(viewHolder.layoutPosition)
                }

                viewHolder.itemView.handle.setOnTouchListener { view, event ->
                    if (AppState.mainViewMode == AppState.MainViewMode.normal) {
                        return@setOnTouchListener true
                    }
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        AppState.startDraggingOnMainViewHolder(viewHolder)
                    }
                    return@setOnTouchListener true
                }

                viewHolder.itemView.setOnTouchListener { view, event ->
                    if (AppState.mainViewMode == AppState.MainViewMode.normal) {
                        return@setOnTouchListener true
                    }
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        AppState.startDraggingOnMainViewHolder(viewHolder)
                    }
                    return@setOnTouchListener true
                }

                viewHolder.itemView.startAnimation(animation)
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
        LocalPreferences.removeSelectedGradeSystem(gradeSystemToDelete, false)

        notifyItemRemoved(index)
    }

    private fun setInterItemSpace(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val marginParams = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (index == 0) {
            marginParams.topMargin = if (Screen.isSparseScreen(GCApp.getInstance().applicationContext)) 8 else 16
            marginParams.bottomMargin = if (Screen.isSparseScreen(GCApp.getInstance().applicationContext)) 6 else 8
        } else if (index == itemCount - 1) {
            marginParams.topMargin = if (Screen.isSparseScreen(GCApp.getInstance().applicationContext)) 6 else 8
            marginParams.bottomMargin = if (Screen.isSparseScreen(GCApp.getInstance().applicationContext)) 8 else 16
        } else {
            marginParams.topMargin = if (Screen.isSparseScreen(GCApp.getInstance().applicationContext)) 6 else 8
            marginParams.bottomMargin = if (Screen.isSparseScreen(GCApp.getInstance().applicationContext)) 6 else 8
        }
        viewHolder.itemView.layoutParams = marginParams
    }
}
