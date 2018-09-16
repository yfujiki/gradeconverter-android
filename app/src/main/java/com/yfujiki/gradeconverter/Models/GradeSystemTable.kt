package com.yfujiki.gradeconverter.Models

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.util.HashMap

object GradeSystemTable {

    lateinit var context: Context
    var tableBody: HashMap<String, GradeSystem> = hashMapOf<String, GradeSystem>()

    public fun assetFile(): File = File("GradeSystemTable.csv")

    public fun driveFile(): File = File(context.filesDir, "GradeSystemTable.csv")

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

        val inputStream = assetFile().inputStream()
        val outputStream = driveFile().outputStream()

        inputStream.bufferedReader().useLines {
            it.forEach {
                outputStream.write(it.toByteArray())
            }
        }

        inputStream.close()
        outputStream.close()
    }

    private fun readContentsOfFile() {
        val inputStream = driveFile().inputStream()

        var lines = listOf<String>()
        inputStream.bufferedReader().useLines {
            lines = it.toList()
        }

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
                tableBody[key]?.grades = gradesOfSameLevel
            }
        }
    }
}