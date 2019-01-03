package com.yfujiki.gradeconverter.Views

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yfujiki.gradeconverter.Adapters.ViewPagerAdapter
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var grade: GradeSystem? = null

    private var disposeBag = CompositeDisposable()

    init {
        LocalPreferences.currentIndexesChanged.subscribe {
            if (LocalPreferences.isBaseGradeSystem(this.grade)) {
                // BaseGradeSystem initiated the change, so don't react to the change you started.
                return@subscribe
            }

            viewPagerAdapter?.reloadDataList()
            itemView.viewPager.adapter = null
            itemView.viewPager.adapter = viewPagerAdapter

            viewPagerAdapter?.currentPosition?.let {
                itemView.viewPager.setCurrentItem(it, false)
            }
        }.addTo(disposeBag)
    }

    var viewPagerAdapter: ViewPagerAdapter? = null

    val valueChangedListener = object : ViewPager.OnPageChangeListener {

        private var jumpPosition = -1

        override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
        ) {
            grade?.let {
                LocalPreferences.setBaseGradeSystem(it)
            }
        }

        override fun onPageSelected(position: Int) {
            if (!LocalPreferences.isBaseGradeSystem(grade)) {
                return
            }

            viewPagerAdapter?.currentPositionUpdated(position)

            if (viewPagerAdapter?.rewindNeeded(position) == true) {
                if (viewPagerAdapter?.rewindData() == true) {
                    jumpPosition = 1
                }
            } else if (viewPagerAdapter?.forwardNeeded(position) == true) {
                if (viewPagerAdapter?.forwardData() == true) {
                    //prepare to jump to the first page
                    jumpPosition = 1
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            //Let's wait for the animation to be completed then do the jump (if we do this in
            //onPageSelected(int position) scroll animation will be canceled).
            if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
                // Work around of some kind of a bug in ViewPager. Won't reload contents otherwise.
                itemView.viewPager.adapter = null
                itemView.viewPager.adapter = viewPagerAdapter

                //Jump without animation so the user is not aware what happened.
                itemView.viewPager.setCurrentItem(jumpPosition, false)
                //Reset jump position.
                jumpPosition = -1
            }
            viewPagerAdapter?.notifyDataSetChanged()
        }
    }

    init {
        itemView.viewPager.addOnPageChangeListener(valueChangedListener)
    }

    fun setGrade(grade: GradeSystem) {
        this.grade = grade
        setGradeName(grade.name)
        setGradeCategory(grade.category)

        if (viewPagerAdapter?.grade == null || viewPagerAdapter?.gradeIsDifferentFrom(grade) == true) {
            viewPagerAdapter = ViewPagerAdapter(grade)
            itemView.viewPager.adapter = viewPagerAdapter
            viewPagerAdapter?.currentPosition?.let {
                itemView.viewPager.setCurrentItem(it, false)
            }
        }
    }

    private fun setGradeName(gradeName: String) {
        itemView.gradeNameTextView.text = gradeName
    }

    private fun setGradeCategory(gradeCategory: String) {
        val drawable: Drawable?
        if (gradeCategory.toLowerCase() == "boulder") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = itemView.context.resources.getDrawable(R.drawable.boulder, null)
            } else {
                drawable = itemView.context.resources.getDrawable(R.drawable.boulder)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = itemView.context.resources.getDrawable(R.drawable.sports, null)
            } else {
                drawable = itemView.context.resources.getDrawable(R.drawable.sports)
            }
        }
        itemView.gradeNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}