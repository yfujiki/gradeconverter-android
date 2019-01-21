package com.responsivebytes.gradeconverter.Models

import io.reactivex.subjects.BehaviorSubject

interface LocalPreferences {

    var selectedGradeSystemsChanged: BehaviorSubject<List<GradeSystem>>
    var currentIndexesChanged: BehaviorSubject<List<Int>>
    var baseGradeSystemChanged: BehaviorSubject<GradeSystem>

    fun setCurrentIndexes(indexes: List<Int>)

    fun currentIndexes(): List<Int>

    fun setSelectedGradeSystems(gradeSystems: List<GradeSystem>, notify: Boolean = true)

    fun addSelectedGradeSystem(gradeSystem: GradeSystem, notify: Boolean = true)

    fun removeSelectedGradeSystem(gradeSystemToRemove: GradeSystem, notify: Boolean = true)

    fun moveGradeSystem(fromIndex: Int, toIndex: Int, notify: Boolean = true)

    fun selectedGradeSystems(): List<GradeSystem>

    fun unselectedGradeSystems(): List<GradeSystem>

    fun selectedGradeSystemNamesCSV(): String

    fun setBaseGradeSystem(gradeSystem: GradeSystem, notify: Boolean = true)

    fun baseGradeSystem(): GradeSystem?

    fun isBaseGradeSystem(gradeSystem: GradeSystem?): Boolean

    fun reset()
}
