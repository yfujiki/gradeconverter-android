package com.responsivebytes.gradeconverter.Models

import android.support.v7.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

interface AppState {
    enum class MainViewMode {
        normal,
        edit
    }

    class DraggingViewHolder {
        var dragging: Boolean = false
        var viewHolder: RecyclerView.ViewHolder

        constructor(sorting: Boolean, viewHolder: RecyclerView.ViewHolder) {
            this.dragging = sorting
            this.viewHolder = viewHolder
        }
    }

    val isTesting: Boolean

    var isShowingAddScreen: Boolean

    val mainViewModeSubject: PublishSubject<MainViewMode>

    val mainViewDraggingViewHolderSubject: PublishSubject<DraggingViewHolder>

    var mainViewMode: MainViewMode

    fun toggleMainViewMode()

    fun startDraggingOnMainViewHolder(viewHolder: RecyclerView.ViewHolder)

    fun stopDraggingOnMainViewHolder(viewHolder: RecyclerView.ViewHolder)
}
