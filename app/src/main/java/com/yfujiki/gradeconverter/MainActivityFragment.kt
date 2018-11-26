package com.yfujiki.gradeconverter

import android.graphics.Canvas
import android.graphics.RectF
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.LEFT
import android.support.v7.widget.helper.ItemTouchHelper.RIGHT
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.Views.MainRecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.adapter = MainRecyclerViewAdapter()
        addSwipeHandler(recyclerView)
    }

    private fun addSwipeHandler(recyclerView: RecyclerView) {

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val mainRecyclerViewAdapter = recyclerView.adapter as MainRecyclerViewAdapter
                if (direction == LEFT) {
                    mainRecyclerViewAdapter.swipeLeft(viewHolder, position)
                } else {
                    mainRecyclerViewAdapter.swipeRight(viewHolder, position)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}

private class MainRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_holder, p0, false)
        return MainRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return LocalPreferences.selectedGradeSystems().size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as MainRecyclerViewHolder
        val grade = LocalPreferences.selectedGradeSystems()[p1]
        viewHolder.setGrade(grade)
        viewHolder.setIndexes(LocalPreferences.currentIndexes(), grade)

        viewHolder.itemView.rightArrow.setOnClickListener {
            swipeLeft(p0, p1)
        }

        viewHolder.itemView.leftArrow.setOnClickListener {
            swipeRight(p0, p1)
        }
    }

    fun swipeLeft(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val gradeSystem = LocalPreferences.selectedGradeSystems()[position]
        val currentIndexes = LocalPreferences.currentIndexes()
        val nextGrade = gradeSystem.higherGradeFromIndexes(currentIndexes)
        moveToNextGrade(nextGrade, gradeSystem)
    }

    fun swipeRight(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val gradeSystem = LocalPreferences.selectedGradeSystems()[position]
        val currentIndexes = LocalPreferences.currentIndexes()
        val nextGrade = gradeSystem.lowerGradeFromIndexes(currentIndexes)
        moveToNextGrade(nextGrade, gradeSystem)
    }

    private fun moveToNextGrade(nextGrade: String?, gradeSystem: GradeSystem) {
        if (nextGrade != null) {
            val nextIndexes = gradeSystem.indexesForGrade(nextGrade)
            LocalPreferences.setCurrentIndexes(nextIndexes)
        }

        notifyDataSetChanged()
    }
}
