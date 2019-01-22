package com.responsivebytes.gradeconverter.Models

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.responsivebytes.gradeconverter.Utilities.Localization
import io.reactivex.subjects.BehaviorSubject

class LocalPreferencesImpl : LocalPreferences {
    companion object {
        val CURRENT_INDEXES_KEY = "com.responsivebytes.gradeConverter.currentIndexes"
        val SELECTED_GRADE_SYSTEMS_KEY = "com.responsivebytes.gradeConverter.selectedGradeSystems"
        val BASE_GRADE_SYSTEM_KEY = "com.responsivebytes.gradeConverter.baseGradeSystem"
        val GRADE_NAME_KEY = "gradeName"
        val GRADE_CATEGORY_KEY = "gradeCategory"

        val DEFAULT_INDEXES = arrayListOf(17)

        val DEFAULT_GRADE_SYSTEM: List<GradeSystem> = when (Localization.currentCountry()) {
            Localization.Country.JP -> arrayListOf(
                    GradeSystemTable.gradeSystemForNameCategory("Ogawayama", "Boulder")!!,
                    GradeSystemTable.gradeSystemForNameCategory("Hueco", "Boulder")!!,
                    GradeSystemTable.gradeSystemForNameCategory("Yosemite Decimal System", "Sports")!!
            )
            else -> arrayListOf(
                    GradeSystemTable.gradeSystemForNameCategory("Hueco", "Boulder")!!,
                    GradeSystemTable.gradeSystemForNameCategory("Fontainebleu", "Boulder")!!,
                    GradeSystemTable.gradeSystemForNameCategory("Yosemite Decimal System", "Sports")!!,
                    GradeSystemTable.gradeSystemForNameCategory("French", "Sports")!!
            )
        }

        val KEY = "com.responsivebytes.gradeconverter.sharedpreferences"
    }

    private var body: SharedPreferences
    private val gson = GsonBuilder().create()

    override var selectedGradeSystemsChanged = BehaviorSubject.create<List<GradeSystem>>()
    override var currentIndexesChanged = BehaviorSubject.create<List<Int>>()
    override var baseGradeSystemChanged = BehaviorSubject.create<GradeSystem>()

    constructor(context: Context, key: String = KEY) {
        this.body = context.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    override fun reset() {
        val editor = this.body.edit()
        editor.clear()
        editor.apply()
    }

    override fun setCurrentIndexes(indexes: List<Int>) {
        val gsonString = gson.toJson(indexes)
        body.edit()
            .putString(CURRENT_INDEXES_KEY, gsonString)
            .apply()
        currentIndexesChanged.onNext(indexes)
    }

    override fun currentIndexes(): List<Int> {
        val gsonString = body.getString(CURRENT_INDEXES_KEY, null)
        if (gsonString == null) {
            return DEFAULT_INDEXES
        }

        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson<List<Int>>(gsonString, type)
    }

    override fun setSelectedGradeSystems(gradeSystems: List<GradeSystem>, notify: Boolean) {
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

    override fun addSelectedGradeSystem(gradeSystem: GradeSystem, notify: Boolean) {
        val gradeSystems = selectedGradeSystems().toMutableList()
        gradeSystems.add(gradeSystem)
        setSelectedGradeSystems(gradeSystems, notify)
    }

    override fun removeSelectedGradeSystem(gradeSystemToRemove: GradeSystem, notify: Boolean) {
        var gradeSystems = selectedGradeSystems()
        gradeSystems = gradeSystems.filter {
            it != gradeSystemToRemove
        }
        setSelectedGradeSystems(gradeSystems, notify)
    }

    override fun moveGradeSystem(fromIndex: Int, toIndex: Int, notify: Boolean) {
        var gradeSystems = selectedGradeSystems().toMutableList()
        if (fromIndex < 0 || fromIndex >= gradeSystems.size) {
            return
        }

        if (toIndex < 0 || toIndex >= gradeSystems.size) {
            return
        }

        val itemToMove = gradeSystems[fromIndex]

        gradeSystems.removeAt(fromIndex)
        gradeSystems.add(toIndex, itemToMove)

        setSelectedGradeSystems(gradeSystems.toList(), notify)
    }

    override fun selectedGradeSystems(): List<GradeSystem> {
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

    override fun unselectedGradeSystems(): List<GradeSystem> {
        return GradeSystemTable.tableBody.values.mapNotNull {
            if (selectedGradeSystems().contains(it)) {
                null
            } else {
                it
            }
        }.sortedBy {
            "${it.category}-${it.name}"
        }
    }

    override fun selectedGradeSystemNamesCSV(): String {
        var string = ""
        selectedGradeSystems().forEach {
            if (string.isNotEmpty()) {
                string += ", "
            }
            string += it.name
        }
        return string
    }

    override fun setBaseGradeSystem(gradeSystem: GradeSystem, notify: Boolean) {
        val name = gradeSystem.name
        val category = gradeSystem.category
        val map = hashMapOf(
                GRADE_NAME_KEY to name,
                GRADE_CATEGORY_KEY to category
        )

        val gsonString = gson.toJson(map)
        body.edit()
                .putString(BASE_GRADE_SYSTEM_KEY, gsonString)
                .apply()

        baseGradeSystemChanged.onNext(gradeSystem)
    }

    override fun baseGradeSystem(): GradeSystem? {
        val gsonString = body.getString(BASE_GRADE_SYSTEM_KEY, null)
        if (gsonString == null) {
            return null
        }

        val type = object : TypeToken<Map<String, String>>() {}.type
        val baseGradeSystem = gson.fromJson<Map<String, String>?>(gsonString, type)

        if (baseGradeSystem == null) {
            return null
        }

        val name = baseGradeSystem[GRADE_NAME_KEY]
        val category = baseGradeSystem[GRADE_CATEGORY_KEY]

        if (name == null || category == null) {
            return null
        } else {
            return GradeSystemTable.gradeSystemForNameCategory(name, category)
        }
    }

    override fun isBaseGradeSystem(gradeSystem: GradeSystem?): Boolean {
        if (gradeSystem == null) {
            return false
        }

        return baseGradeSystem() == gradeSystem
    }
}
