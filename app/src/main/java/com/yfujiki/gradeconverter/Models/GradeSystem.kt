package com.yfujiki.gradeconverter.Models

import android.content.Context
import com.yfujiki.gradeconverter.Utilities.Localization

class GradeSystem : Comparable<GradeSystem> {
    val name: String
    val category: String
    val countryCodes: List<String>
    var grades: List<String> = listOf<String>()
    var isBaseSystem: Boolean = false

    constructor(name: String, category: String, countryCodes: List<String>) {
        this.name = name
        this.category = category
        this.countryCodes = countryCodes
    }

    fun key(): String = "$name-$category"

    fun addGrade(grade: String) {
        var mutableList = grades.toMutableList()
        mutableList.add(grade)
        grades = mutableList
    }

    fun gradeAtIndex(index: Int, higher: Boolean): String {
        var convertedIndex = index
        if (convertedIndex < 0) {
            convertedIndex = 0
        }
        if (convertedIndex >= grades.count()) {
            convertedIndex = grades.count() - 1
        }

        var grade = grades[convertedIndex]

        if (grade.isEmpty()) {
            // There is no corresponding entry at that specific index
            if (higher) {
                higherGradeAtIndex(index)?.let {
                    grade = it
                }
            } else {
                lowerGradeAtIndex(index)?.let {
                    grade = it
                }
            }
        }

        return grade
    }

    fun localizedGradeAtIndexes(indexes: List<Int>, context: Context): String {
        val sortedIndexes = indexes.sorted()

        val lowGrade = gradeAtIndex(sortedIndexes[0], false)
        val highGrade = gradeAtIndex(sortedIndexes[indexes.count() - 1], true)

        if (lowGrade == highGrade) {
            Localization.stringResourceFor(lowGrade)?.let {
                return context.getString(it)
            } ?: run {
                return lowGrade
            }
        } else if (lowGrade.isEmpty()) {
            Localization.stringResourceFor(highGrade)?.let {
                return "~ ${context.getString(it)}"
            } ?: run {
                return "~ $highGrade"
            }
        } else if (highGrade.isEmpty()) {
            Localization.stringResourceFor(lowGrade)?.let {
                return "${context.getString(it)} ~"
            } ?: run {
                return "$lowGrade ~"
            }
        } else if (areAdjacentGrades(lowGrade, highGrade)) {
            var lowGradeString = ""
            var highGradeString = ""
            Localization.stringResourceFor(lowGrade)?.let {
                lowGradeString = context.getString(it)
            } ?: run {
                lowGradeString = lowGrade
            }
            Localization.stringResourceFor(highGrade)?.let {
                highGradeString = context.getString(it)
            } ?: run {
                highGradeString = highGrade
            }
            return "$lowGradeString/$highGradeString"
        } else {
            var lowGradeString = ""
            var highGradeString = ""
            Localization.stringResourceFor(lowGrade)?.let {
                lowGradeString = context.getString(it)
            } ?: run {
                lowGradeString = lowGrade
            }
            Localization.stringResourceFor(highGrade)?.let {
                highGradeString = context.getString(it)
            } ?: run {
                highGradeString = highGrade
            }
            return "$lowGradeString ~ $highGradeString"
        }
    }

    fun higherGradeAtIndex(index: Int): String? {
        for (i in index until grades.count()) {
            if (grades[i].isNotEmpty()) {
                return grades[i]
            }
        }
        return null
    }

    fun lowerGradeAtIndex(index: Int): String? {
        for (i in (0 until index).reversed()) {
            if (grades[i].isNotEmpty()) {
                return grades[i]
            }
        }
        return null
    }

    fun indexesForGrade(grade: String): List<Int> {
        var indexes = listOf<Int>().toMutableList()

        for (i in 0 until grades.count()) {
            if (grades[i] == grade) {
                indexes.add(i)
            }
        }

        return indexes
    }

    fun higherGradeFromIndexes(indexes: List<Int>): String? {
        return nextGradeFromIndexes(indexes, true)
    }

    fun lowerGradeFromIndexes(indexes: List<Int>): String? {
        return nextGradeFromIndexes(indexes, false)
    }

    private fun nextGradeFromIndexes(indexes: List<Int>, higher: Boolean): String? {
        val sortedIndexes = indexes.sorted()

        val lowGrade = gradeAtIndex(sortedIndexes[0], false)
        val highGrade = gradeAtIndex(sortedIndexes[indexes.count() - 1], true)

        var nextGrade: String? = null

        if (lowGrade == highGrade) {
            if (higher) {
                for (i in sortedIndexes[indexes.count() - 1] until grades.count()) {
                    if (grades[i].isNotEmpty() && grades[i] != highGrade) {
                        nextGrade = grades[i]
                        break
                    }
                }
            } else {
                for (i in (0..sortedIndexes[0]).reversed()) {
                    if (grades[i].isNotEmpty() && grades[i] != lowGrade) {
                        nextGrade = grades[i]
                        break
                    }
                }
            }
        } else {
            if (higher && highGrade.isNotEmpty()) {
                nextGrade = highGrade
            } else if (!higher && lowGrade.isNotEmpty()) {
                nextGrade = lowGrade
            }
        }

        return nextGrade
    }

    // Assuming lowGrade and highGrade are different grade strings and order is low => high.
    private fun areAdjacentGrades(lowGrade: String, highGrade: String): Boolean {
        val lowIndexes = indexesForGrade(lowGrade)
        val nextToLowGrade = higherGradeFromIndexes(lowIndexes)

        return nextToLowGrade == highGrade
    }

    override fun compareTo(other: GradeSystem): Int {
        var result = name.compareTo(other.name)
        if (result != 0) {
            return result
        }

        result = category.compareTo(other.category)
        return result
    }
}
