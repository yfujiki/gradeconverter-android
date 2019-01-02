package com.yfujiki.gradeconverter.Models

import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class AppState {
    enum class MainViewMode {
        normal,
        edit
    }

    companion object {
        val mainViewModeSubject: PublishSubject<MainViewMode> = PublishSubject.create()

        var mainViewMode: MainViewMode by Delegates.observable(MainViewMode.normal) {
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
    }
}