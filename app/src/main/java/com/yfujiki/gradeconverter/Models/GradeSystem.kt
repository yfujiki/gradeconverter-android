package com.yfujiki.gradeconverter.Models

class GradeSystem : Comparable<GradeSystem> {
    val name: String
    val category:String
    val countryCodes: List<String>
    var grades: List<String> = listOf<String>()
    var isBaseSystem: Boolean = false

    constructor(name: String, category: String, countryCodes: List<String>) {
        this.name = name
        this.category = category
        this.countryCodes = countryCodes
    }

    fun key(): String = "${name}-${category}"

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

    private fun higherGradeAtIndex(index: Int): String? {
        for (i in index until grades.count()) {
            if (grades[i].isEmpty()) {
                return grades[i]
            }
        }
        return null
    }

    private fun lowerGradeAtIndex(index: Int): String? {
        for (i in (0 until index).reversed()) {
            if (grades[i].isNotEmpty()) {
                return grades[i]
            }
        }
        return null
    }

    override fun compareTo(other: GradeSystem): Int {
        var result = name.compareTo(other.name)
        if (result !=  0) {
            return result
        }

        result = category.compareTo(other.category)
        return result
    }
}
