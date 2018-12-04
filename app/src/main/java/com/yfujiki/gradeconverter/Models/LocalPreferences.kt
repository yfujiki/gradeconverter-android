package com.yfujiki.gradeconverter.Models

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yfujiki.gradeconverter.Utilities.Localization
import io.reactivex.subjects.PublishSubject

object LocalPreferences {
    val CURRENT_INDEXES_KEY = "com.yfujiki.gradeConverter.currentIndexes"
    val SELECTED_GRADE_SYSTEMS_KEY = "com.yfujiki.gradeConverter.selectedGradeSystems"
    val GRADE_NAME_KEY = "gradeName"
    val GRADE_CATEGORY_KEY = "gradeCategory"

    val DEFAULT_INDEXES = arrayListOf(17)

    val DEFAULT_GRADE_SYSTEM: List<GradeSystem> = when (Localization.currentCountry()) {
        Localization.Country.JP -> arrayListOf(
                GradeSystemTable.gradeSystemForNameCategory("Ogawayama", "Boulder")!!,
                GradeSystemTable.gradeSystemForNameCategory("Hueco", "Boulder")!!,
                GradeSystemTable.gradeSystemForNameCategory("Yosemite Decimal System", "Boulder")!!
            )
        else -> arrayListOf(
                GradeSystemTable.gradeSystemForNameCategory("Hueco", "Boulder")!!,
                GradeSystemTable.gradeSystemForNameCategory("Fontainebleu", "Boulder")!!,
                GradeSystemTable.gradeSystemForNameCategory("Yosemite Decimal System", "Sports")!!,
                GradeSystemTable.gradeSystemForNameCategory("French", "Sports")!!
            )
    }

    val KEY = "com.yfujiki.gradeconverter.sharedpreferences"

    private lateinit var body: SharedPreferences
    private val gson = GsonBuilder().create()

    public var selectedGradeSystemsChanged = PublishSubject.create<List<GradeSystem>>()
    public var currentIndexesChanged = PublishSubject.create<List<Int>>()

    fun init(context: Context) {
        this.body = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
    }

    fun setCurrentIndexes(indexes: List<Int>) {
        val gsonString = gson.toJson(indexes)
        body.edit()
            .putString(CURRENT_INDEXES_KEY, gsonString)
            .apply()
        currentIndexesChanged.onNext(indexes)
    }

    fun currentIndexes(): List<Int> {
        val gsonString = body.getString(CURRENT_INDEXES_KEY, null)
        if (gsonString == null) {
            return listOf()
        }

        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson<List<Int>>(gsonString, type)
    }

    fun setSelectedGradeSystems(gradeSystems: List<GradeSystem>, notify: Boolean = true) {
        val systemKeys = gradeSystems.map {
            val name = it.name
            val category = it.category
            hashMapOf(
                    GRADE_NAME_KEY to name,
                    GRADE_CATEGORY_KEY to category
            )
        }

        val gsonString = gson.toJson(systemKeys)
        body.edit()
            .putString(SELECTED_GRADE_SYSTEMS_KEY, gsonString)
            .apply()

        if (notify) {
            selectedGradeSystemsChanged.onNext(gradeSystems)
        }
    }

    fun addSelectedGradeSystem(gradeSystem: GradeSystem, notify: Boolean = true) {
        val gradeSystems = selectedGradeSystems().toMutableList()
        gradeSystems.add(gradeSystem)
        setSelectedGradeSystems(gradeSystems, notify)
    }

    fun removeSelectedGradeSystem(gradeSystemToRemove: GradeSystem, notify: Boolean = true) {
        var gradeSystems = selectedGradeSystems()
        gradeSystems = gradeSystems.filter {
            it != gradeSystemToRemove
        }
        setSelectedGradeSystems(gradeSystems, notify)
    }

    fun selectedGradeSystems(): List<GradeSystem> {
        val gsonString = body.getString(SELECTED_GRADE_SYSTEMS_KEY, null)
        if (gsonString == null) {
            return DEFAULT_GRADE_SYSTEM
        }

        val type = object : TypeToken<List<Map<String, String>>>() {}.type
        val gradeSystems = gson.fromJson<List<Map<String, String>>>(gsonString, type)

        return gradeSystems.mapNotNull {
            val name = it[GRADE_NAME_KEY]
            val category = it[GRADE_CATEGORY_KEY]

            if (name == null || category == null) {
                null
            } else {
                GradeSystemTable.gradeSystemForNameCategory(name, category)
            }
        }
    }

    fun selectedGradeSystemNamesCSV(): String {
        var string = ""
        selectedGradeSystems().forEach {
            if (string.isNotEmpty()) {
                string += ", "
            }
            string += it.name
        }
        return string
    }
}
