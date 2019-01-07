package com.responsivebytes.gradeconverter.Views

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.responsivebytes.gradeconverter.Adapters.ViewPagerAdapter
import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.AppState
import com.responsivebytes.gradeconverter.Models.GradeSystem
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.recycler_view_holder.view.*
import java.lang.ref.WeakReference

class MainRecyclerViewHolder(itemView: View, val activityDisposable: CompositeDisposable) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    private var grade: GradeSystem? = null

    private var disposable = CompositeDisposable()

    var viewPagerAdapter: ViewPagerAdapter? = null

    val valueChangedListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

        private var jumpPosition = -1

        override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            if (!LocalPreferences.isBaseGradeSystem(grade)) {
                return
            }

            viewPagerAdapter?.currentPositionUpdated(position)

            if (viewPagerAdapter?.isInPositionForRewind(position) == true) {
                if (viewPagerAdapter?.rewindData() == true) {
                    jumpPosition = 1
                } else {
                    jumpPosition = position
                }
            } else if (viewPagerAdapter?.isInPositionForForwarding(position) == true) {
                if (viewPagerAdapter?.forwardData() == true) {
                    jumpPosition = 1
                } else {
                    jumpPosition = position
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            //Let's wait for the animation to be completed then do the jump (if we do this in
            //onPageSelected(int position) scroll animation will be canceled).
            if (state == androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE) {
                if (jumpPosition >= 0) {
                    // Work around of some kind of a bug in ViewPager. Won't reload contents otherwise.
                    viewPagerAdapter?.notifyDataSetChanged()
                    itemView.viewPager.adapter = null
                    itemView.viewPager.adapter = viewPagerAdapter

                    itemView.viewPager.setCurrentItem(jumpPosition, false)

                    jumpPosition = -1
                }
                configureLeftRightButton()
            } else if (state == androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING) {
                grade?.let {
                    LocalPreferences.setBaseGradeSystem(it)
                }
            }
        }
    }

    init {
        val weakSelf = WeakReference(this)
        val weakItemView = WeakReference(itemView)

        disposable += LocalPreferences.currentIndexesChanged.subscribe {

            if (LocalPreferences.isBaseGradeSystem(this.grade)) {
                // BaseGradeSystem initiated the change, so don't react to the change you started.
                return@subscribe
            }

            weakSelf.get()?.viewPagerAdapter?.reloadDataList()
            weakSelf.get()?.viewPagerAdapter?.notifyDataSetChanged()
            weakItemView.get()?.viewPager?.adapter = viewPagerAdapter

            weakSelf.get()?.viewPagerAdapter?.currentPosition?.let {
                weakItemView.get()?.viewPager?.setCurrentItem(it, false)
            }

            weakSelf.get()?.configureLeftRightButton()
        }

        disposable += LocalPreferences.baseGradeSystemChanged.subscribe {
            weakSelf.get()?.configureBackground()
        }

        disposable += AppState.mainViewModeSubject.subscribe {
            weakItemView.get()?.viewPager?.clearOnPageChangeListeners()

            if (it == AppState.MainViewMode.normal) {
                weakItemView.get()?.viewPager?.swipeLocked = false
                weakItemView.get()?.viewPager?.addOnPageChangeListener(valueChangedListener)
            } else {
                weakItemView.get()?.viewPager?.swipeLocked = true
            }

            weakSelf.get()?.configureBackground()
        }

        activityDisposable += disposable

        itemView.viewPager.addOnPageChangeListener(valueChangedListener)
    }

    protected fun finalize() {
        if (!disposable.isDisposed()) {
            disposable.dispose()
        }
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
            configureLeftRightButton()
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

    private fun configureBackground() {
        if (AppState.mainViewMode == AppState.MainViewMode.normal && grade == LocalPreferences.baseGradeSystem()) {
            itemView.background = AppCompatResources.getDrawable(GCApp.getInstance().applicationContext, R.drawable.rounded_rect_with_border)
        } else {
            itemView.background = AppCompatResources.getDrawable(GCApp.getInstance().applicationContext, R.drawable.rounded_rect_shape)
        }
    }

    private fun configureLeftRightButton() {
        if (viewPagerAdapter?.hasLowerGrades() == true) {
            itemView.leftArrow.isEnabled = true
            itemView.leftArrow.alpha = 1.0f
        } else {
            itemView.leftArrow.isEnabled = false
            itemView.leftArrow.alpha = 0.2f
        }

        if (viewPagerAdapter?.hasHigherGrades() == true) {
            itemView.rightArrow.isEnabled = true
            itemView.rightArrow.alpha = 1.0f
        } else {
            itemView.rightArrow.isEnabled = false
            itemView.rightArrow.alpha = 0.2f
        }
    }

    fun scrollRight() {
        grade?.let {
            LocalPreferences.setBaseGradeSystem(it, true)
        }

        if (viewPagerAdapter?.hasHigherGrades() == true) {
            val rightItem = viewPagerAdapter!!.currentPosition + 1
            itemView.viewPager.setCurrentItem(rightItem, true)
        }
    }

    fun scrollLeft() {
        grade?.let {
            LocalPreferences.setBaseGradeSystem(it, true)
        }

        if (viewPagerAdapter?.hasLowerGrades() == true) {
            val leftItem = viewPagerAdapter!!.currentPosition - 1
            itemView.viewPager.setCurrentItem(leftItem, true)
        }
    }
}