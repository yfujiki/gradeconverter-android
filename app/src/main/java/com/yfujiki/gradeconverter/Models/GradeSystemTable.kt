package com.yfujiki.gradeconverter.Models

import android.content.Context
import java.io.File
import java.io.InputStream
import java.util.HashMap

object GradeSystemTable {

    lateinit var context: Context
    var tableBody: HashMap<String, GradeSystem> = hashMapOf<String, GradeSystem>()

    private fun assetFileInputStream(): InputStream = context.assets.open("GradeSystemTable.csv")

    private fun driveFile(): File = File(context.filesDir, "GradeSystemTable.csv")

    // We first need to call this method in order to use this singleton
    fun init(context: Context) {
        this.context = context

        moveFileToDrive()
        readContentsOfFile()
    }

    private fun moveFileToDrive() {
        if (driveFile().exists()) {
            return
        }

        val inputStream = assetFileInputStream()
        var inputReader = inputStream.bufferedReader()
        val outputWriter = driveFile().bufferedWriter()

        inputReader.useLines {
            it.forEach {
                outputWriter.write(it)
                outputWriter.newLine()
            }
        }

        inputStream.close()
        outputWriter.close()
    }

    private fun readContentsOfFile() {
        val inputStream = driveFile().inputStream()

        var lines = listOf<String>()
        inputStream.bufferedReader().useLines {
            it.forEach {
                var mutableList = lines.toMutableList()
                mutableList.add(it)
                lines = mutableList
            }
        }

        inputStream.close()

        val names = lines[0].split(",")
        val categories = lines[1].split(",")
        val arrayOfLocales = lines[2].split(",")
        val arrayOfGrades = lines.slice(3 until lines.count())

        for (i in 0 until names.count()) {
            val locales = arrayOfLocales.get(i).split(" ")
            val gradeSystem = GradeSystem(names[i], categories[i], locales)
            tableBody[gradeSystem.key()] = gradeSystem
        }

        for (grades in arrayOfGrades) {
            if (grades.length == 0) {
                continue
            }

            val gradesOfSameLevel = grades.split(",")

            for(i in 0 until names.count()) {
                val key = "${names[i]}-${categories[i]}"
                tableBody[key]?.addGrade(gradesOfSameLevel[i])
            }
        }
    }

    fun gradeSystemForNameCategory(name: String, category: String): GradeSystem? = tableBody["${name}-${category}"]

    fun gradeSystemsForCountryCode(countryCode: String):List<GradeSystem> {
        return tableBody.filter {
            it.value.countryCodes.contains(countryCode)
        }.values.sortedBy {
            it.name
        }
    }
}