package com.responsivebytes.gradeconverter.Models

import android.support.v7.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

open class AppStateImpl : AppState {
    override val isTesting = false

    override val mainViewModeSubject: PublishSubject<AppState.MainViewMode> = PublishSubject.create()

    override val mainViewDraggingViewHolderSubject: PublishSubject<AppState.DraggingViewHolder> = PublishSubject.create()

    override var mainViewMode: AppState.MainViewMode by Delegates.observable(AppState.MainViewMode.normal) {
        property, oldValue, newValue ->
        mainViewModeSubject.onNext(newValue)
    }

    override fun toggleMainViewMode() {
        mainViewMode = when (mainViewMode) {
            AppState.MainViewMode.normal -> AppState.MainViewMode.edit
            AppState.MainViewMode.edit -> AppState.MainViewMode.normal
        }
    }

    override fun startDraggingOnMainViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val draggingViewHolder = AppState.DraggingViewHolder(true, viewHolder)
        mainViewDraggingViewHolderSubject.onNext(draggingViewHolder)
    }

    override fun stopDraggingOnMainViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val noDraggingViewHolder = AppState.DraggingViewHolder(false, viewHolder)
        mainViewDraggingViewHolderSubject.onNext(noDraggingViewHolder)
    }
}