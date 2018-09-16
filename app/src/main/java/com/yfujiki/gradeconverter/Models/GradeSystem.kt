package com.yfujiki.gradeconverter.Models

class GradeSystem : Comparable<GradeSystem> {
    val name: String
    val category:String
    val countryCodes: List<String>
    var grades: List<String> = listOf<String>()
    var isBaseSystem: Boolean = false

    public constructor(name: String, category: String, countryCodes: List<String>) {
        this.name = name
        this.category = category
        this.countryCodes = countryCodes
    }

    public fun key(): String = "${name}-${category}"

    public fun addGrade(grade: String) {
        grades.toMutableList().add(grade)
    }

    public override fun compareTo(other: GradeSystem): Int {
        var result = name.compareTo(other.name)
        if (result !=  0) {
            return result
        }

        result = category.compareTo(other.category)
        return result
    }
}
