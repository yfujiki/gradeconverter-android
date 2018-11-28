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
import com.yfujiki.gradeconverter.Adapters.MainRecyclerViewAdapter
import com.yfujiki.gradeconverter.Models.LocalPreferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    var disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.adapter = MainRecyclerViewAdapter()
        addSwipeHandler(recyclerView)

        LocalPreferences.selectedGradeSystemsChanged.subscribe {
            (recyclerView.adapter as MainRecyclerViewAdapter).notifyDataSetChanged()
        }.addTo(disposable)

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
