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

    private var dataList: MutableList<String>

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

    fun rewindNeeded(position: Int): Boolean {
        return position == 0
    }

    fun forwardNeeded(position: Int): Boolean {
        return position == dataList.count() - 1
    }

    fun rewindData(): Boolean {
        if (dataList.size == 0) {
            return false
        }

        val gradeForP1 = dataList.get(0)
        val indexesForP1 = grade.indexesForGrade(gradeForP1)

        val dataListAndCurrentPosition = dataListForIndexes(indexesForP1)
        dataList = dataListAndCurrentPosition.first
        currentPosition = dataListAndCurrentPosition.second

//        notifyDataSetChanged()

        return dataListAndCurrentPosition.second == 1
    }

    fun forwardData(): Boolean {
        if (dataList.size == 0) {
            return false
        }

        val gradeForP1 = dataList.last()
        val indexesForP1 = grade.indexesForGrade(gradeForP1)

        val dataListAndCurrentPosition = dataListForIndexes(indexesForP1)
        dataList = dataListAndCurrentPosition.first
        currentPosition = dataListAndCurrentPosition.second

//        notifyDataSetChanged()

        return dataListAndCurrentPosition.second == 1
    }

    fun reloadDataList() {
        val dataListAndCurrentPosition = dataListFromCurrentIndex()
        dataList = dataListAndCurrentPosition.first
        currentPosition = dataListAndCurrentPosition.second

//        notifyDataSetChanged()
    }

    fun currentPositionUpdated(currentPosition: Int) {
        if (dataList.size <= currentPosition) {
            return
        }

        val selectedGrade = dataList.get(currentPosition)
        val selectedIndexes = grade.indexesForGrade(selectedGrade)

        if (LocalPreferences.isBaseGradeSystem(grade)) {
            LocalPreferences.setCurrentIndexes(selectedIndexes)
        }
    }

    private fun dataListForIndexes(indexes: List<Int>): Pair<MutableList<String>, Int> {
        var data = mutableListOf<String>()

        val context = GCApp.getInstance().applicationContext

        val indexesForP2 = indexes

        var hasLowerGrade = false
        var hasHigherGrade = false
        grade.lowerGradeFromIndexes(indexesForP2)?.let {
            val indexesForP1 = grade.indexesForGrade(it)
            data.add(grade.localizedGradeAtIndexes(indexesForP1, context)) // 1 page
            hasLowerGrade = true
        }
        data.add(grade.localizedGradeAtIndexes(indexesForP2, context)) // 2 page
        grade.higherGradeFromIndexes(indexesForP2)?.let {
            val indexesForP3 = grade.indexesForGrade(it)
            data.add(grade.localizedGradeAtIndexes(indexesForP3, context)) // 3 page
            hasHigherGrade = true
        }

        var currentPosition = 1
        if (!hasHigherGrade && !hasLowerGrade) {
            currentPosition = 0
        } else if (!hasHigherGrade) {
            currentPosition = 1
        } else if (!hasLowerGrade) {
            currentPosition = 0
        }

        return Pair(data, currentPosition)
    }

    private fun dataListFromCurrentIndex(): Pair<MutableList<String>, Int> {
        return dataListForIndexes(LocalPreferences.currentIndexes())
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return dataList.count()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val view = LayoutInflater.from(GCApp.getInstance().applicationContext)
                .inflate(R.layout.view_pager_view_holder, container, false)

        if (dataList.count() > position) {
            view.gradeValueTextView.text = dataList.get(position)!!
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}