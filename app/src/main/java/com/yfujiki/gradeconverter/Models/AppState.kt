package com.yfujiki.gradeconverter.Models

import android.support.v7.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class AppState {
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

    companion object {
        val mainViewModeSubject: PublishSubject<MainViewMode> = PublishSubject.create()

        val mainViewDraggingViewHolderSubject: PublishSubject<DraggingViewHolder> = PublishSubject.create()

        var mainViewMode: MainViewMode by Delegates.observable(MainViewMode.normal){
            property, oldValue, newValue ->
            mainViewModeSubject.onNext(newValue)
        }
            private set

        fun toggleMainViewMode() {
            mainViewMode = when (mainViewMode) {
                MainViewMode.normal -> MainViewMode.edit
                MainViewMode.edit -> MainViewMode.normal
            }
        }

        fun startDraggingOnMainViewHolder(viewHolder: RecyclerView.ViewHolder) {
            val draggingViewHolder = DraggingViewHolder(true, viewHolder)
            mainViewDraggingViewHolderSubject.onNext(draggingViewHolder)
        }

        fun stopDraggingOnMainViewHolder(viewHolder: RecyclerView.ViewHolder) {
            val noDraggingViewHolder = DraggingViewHolder(false, viewHolder)
            mainViewDraggingViewHolderSubject.onNext(noDraggingViewHolder)
        }
    }
}