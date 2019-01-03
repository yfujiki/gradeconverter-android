package com.yfujiki.gradeconverter.Adapters

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yfujiki.gradeconverter.GCApp
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.R
import kotlinx.android.synthetic.main.view_pager_view_holder.view.*

class ViewPagerAdapter(val grade: GradeSystem) : PagerAdapter() {

    private var dataList: MutableList<List<Int>>

    var currentPosition: Int = 1
        private set

    init {
        val dataListAndPosition = dataListFromCurrentIndex()
        dataList = dataListAndPosition.first
        currentPosition = dataListAndPosition.second
    }

    fun gradeIsDifferentFrom(otherGrade: GradeSystem): Boolean {
        return grade != otherGrade
    }

    fun isInPositionForRewind(position: Int): Boolean {
        return position == 0
    }

    fun isInPositionForForwarding(position: Int): Boolean {
        return position == dataList.count() - 1
    }

    fun hasLowerGrades(): Boolean {
        if (dataList.size == 0) {
            return false
        }

        val currentGradeIndexes = dataList.get(currentPosition)
        return grade.lowerGradeFromIndexes(currentGradeIndexes) != null
    }

    fun hasHigherGrades(): Boolean {
        if (dataList.size == 0) {
            return false
        }

        val currentGradeIndexes = dataList.get(currentPosition)
        return grade.higherGradeFromIndexes(currentGradeIndexes) != null
    }

    fun rewindData(): Boolean {
        if (dataList.size == 0) {
            return false
        }

        val firstGradeIndexes = dataList.get(0)

        val dataListAndCurrentPosition = dataListForIndexes(firstGradeIndexes)
        dataList = dataListAndCurrentPosition.first
        currentPosition = dataListAndCurrentPosition.second

        return dataListAndCurrentPosition.second == 1
    }

    fun forwardData(): Boolean {
        if (dataList.size == 0) {
            return false
        }

        val lastGradeIndexes = dataList.last()

        val dataListAndCurrentPosition = dataListForIndexes(lastGradeIndexes)
        dataList = dataListAndCurrentPosition.first
        currentPosition = dataListAndCurrentPosition.second

        return dataListAndCurrentPosition.second == 1
    }

    fun reloadDataList() {
        val dataListAndCurrentPosition = dataListFromCurrentIndex()
        dataList = dataListAndCurrentPosition.first
        currentPosition = dataListAndCurrentPosition.second
    }

    fun currentPositionUpdated(currentPosition: Int) {
        if (dataList.size <= currentPosition) {
            return
        }

        this.currentPosition = currentPosition

        val selectedIndexes = dataList.get(currentPosition)

        if (LocalPreferences.isBaseGradeSystem(grade)) {
            LocalPreferences.setCurrentIndexes(selectedIndexes)
        }
    }

    private fun dataListForIndexes(indexes: List<Int>): Pair<MutableList<List<Int>>, Int> {
        var data = mutableListOf<List<Int>>()

        val indexesForP2 = indexes

        val gradeForP1 = grade.lowerGradeFromIndexes(indexesForP2)
        val indexesForP1 = if (gradeForP1 != null) grade.indexesForGrade(gradeForP1) else null
        var hasLowerGrade = false
        if (indexesForP1 != null) {
            data.add(indexesForP1) // 1 page
            hasLowerGrade = true
        }

        data.add(indexesForP2) // 2 page

        val gradeForP3 = grade.higherGradeFromIndexes(indexesForP2)
        val indexesForP3 = if (gradeForP3 != null) grade.indexesForGrade(gradeForP3) else null
        var hasHigherGrade = false
        if (indexesForP3 != null) {
            data.add(indexesForP3) // 3 page
            hasHigherGrade = true
        }

        var currentPosition = 1
        if (!hasHigherGrade && !hasLowerGrade) {
            currentPosition = 0
        } else if (!hasHigherGrade) {
            currentPosition = 1
            if (indexesForP1 != null) {
                grade.lowerGradeFromIndexes(indexesForP1)?.let {
                    data.add(0, grade.indexesForGrade(it))
                    currentPosition = 2
                }
            }
        } else if (!hasLowerGrade) {
            currentPosition = 0
            if (indexesForP3 != null) {
                grade.higherGradeFromIndexes(indexesForP3)?.let {
                    data.add(grade.indexesForGrade(it))
                }
            }
        }

        return Pair(data, currentPosition)
    }

    private fun dataListFromCurrentIndex(): Pair<MutableList<List<Int>>, Int> {
        return dataListForIndexes(LocalPreferences.currentIndexes())
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return dataList.count()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val context = GCApp.getInstance().applicationContext
        val view = LayoutInflater.from(context)
                .inflate(R.layout.view_pager_view_holder, container, false)

        if (dataList.count() > position) {
            val indexes = dataList.get(position)!!
            view.gradeValueTextView.text = grade.localizedGradeAtIndexes(indexes, context)
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}