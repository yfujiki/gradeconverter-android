package com.responsivebytes.gradeconverter.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.AppState
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.R
import com.responsivebytes.gradeconverter.Utilities.Screen
import com.responsivebytes.gradeconverter.Views.MainRecyclerViewHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewAdapter(val localPreferences: LocalPreferences,
                              var appState: AppState,
                              val activityDisposable: CompositeDisposable) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val animation by lazy {
        AnimationUtils.loadAnimation(GCApp.getInstance().applicationContext, R.anim.wobble)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_holder, p0, false)

        return MainRecyclerViewHolder(itemView, localPreferences, appState, activityDisposable)
    }

    override fun getItemCount(): Int {
        return localPreferences.selectedGradeSystems().size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as MainRecyclerViewHolder
        val grade = localPreferences.selectedGradeSystems()[p1]
        viewHolder.setGrade(grade)
        viewHolder.configureBackground()

        setInterItemSpace(viewHolder, p1)

        when (appState.mainViewMode) {
            AppState.MainViewMode.normal -> {
                viewHolder.itemView.deleteButton.visibility = View.GONE
                viewHolder.itemView.handle.visibility = View.GONE
                viewHolder.itemView.leftArrow.visibility = View.VISIBLE
                viewHolder.itemView.rightArrow.visibility = View.VISIBLE

                viewHolder.itemView.rightArrow.setOnClickListener {
                    viewHolder.scrollRight()
                }

                viewHolder.itemView.leftArrow.setOnClickListener {
                    viewHolder.scrollLeft()
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
                    if (appState.mainViewMode == AppState.MainViewMode.normal) {
                        return@setOnTouchListener true
                    }
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        appState.startDraggingOnMainViewHolder(viewHolder)
                    }
                    return@setOnTouchListener true
                }

                viewHolder.itemView.setOnLongClickListener {
                    if (appState.mainViewMode == AppState.MainViewMode.normal) {
                        return@setOnLongClickListener true
                    }
                    appState.startDraggingOnMainViewHolder(viewHolder)

                    return@setOnLongClickListener true
                }

                if (!appState.isTesting) {
                    viewHolder.itemView.startAnimation(animation)
                }
            }
        }
    }

    private fun deleteItemAt(index: Int) {
        val gradeSystemToDelete = localPreferences.selectedGradeSystems()[index]
        localPreferences.removeSelectedGradeSystem(gradeSystemToDelete)

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
