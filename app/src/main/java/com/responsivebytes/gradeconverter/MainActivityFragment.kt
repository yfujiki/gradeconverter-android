package com.responsivebytes.gradeconverter

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.responsivebytes.gradeconverter.Adapters.MainRecyclerViewAdapter
import com.responsivebytes.gradeconverter.Models.AppState
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    private var disposable = CompositeDisposable()

    @Inject
    lateinit var localPreferences: LocalPreferences

    @Inject
    lateinit var appState: AppState

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, 0) {
            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                // We don't support swipe
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                if (viewHolder == target) {
                    return false
                }

                val fromPosition = viewHolder.layoutPosition
                val toPosition = target.layoutPosition

                localPreferences.moveGradeSystem(fromPosition, toPosition, false)

                recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

                return true
            }

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return when (appState.mainViewMode) {
                    AppState.MainViewMode.edit -> ItemTouchHelper.Callback.makeMovementFlags(
                            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                            0)
                    AppState.MainViewMode.normal -> ItemTouchHelper.Callback.makeMovementFlags(
                            0,
                            0)
                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.8f
                }
                super.onSelectedChanged(viewHolder, actionState)
            }

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                viewHolder.itemView.alpha = 1.0f
                appState.stopDraggingOnMainViewHolder(viewHolder)
                super.clearView(recyclerView, viewHolder)
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        subscribeToAppState()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.adapter = MainRecyclerViewAdapter(localPreferences, appState, (activity as MainActivity).disposable)

        addTouchHandler(recyclerView)

        disposable += localPreferences.selectedGradeSystemsChanged.subscribe {
            if (appState.isShowingAddScreen) {
                // We want to receive events from Add Screen
                (recyclerView.adapter as MainRecyclerViewAdapter).notifyDataSetChanged()
            }
        }
    }

    private fun addTouchHandler(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun subscribeToAppState() {
        disposable += appState.mainViewModeSubject.subscribe {
            recyclerView.adapter?.notifyDataSetChanged()
        }

        disposable += appState.mainViewDraggingViewHolderSubject.subscribe {
            if (it.dragging) {
                itemTouchHelper.startDrag(it.viewHolder)
            }
        }
    }
}
