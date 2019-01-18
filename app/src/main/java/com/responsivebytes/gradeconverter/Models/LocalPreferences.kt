package com.responsivebytes.gradeconverter.Models

import io.reactivex.subjects.PublishSubject

interface LocalPreferences {

    var selectedGradeSystemsChanged: PublishSubject<List<GradeSystem>>
    var currentIndexesChanged: PublishSubject<List<Int>>
    var baseGradeSystemChanged: PublishSubject<GradeSystem>

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
