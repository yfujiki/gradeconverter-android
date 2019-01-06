package com.yfujiki.gradeconverter

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.yfujiki.gradeconverter.Adapters.AddRecyclerViewAdapter
import com.yfujiki.gradeconverter.Models.AppState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.action_bar_title_view.view.*
import kotlinx.android.synthetic.main.activity_add.view.*
import kotlinx.android.synthetic.main.activity_info.view.*

import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var addDialog: AlertDialog? = null
    var infoDialog: AlertDialog? = null

    val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            openAddAlertDialog()
        }

        customizeTitleView()
        subscribeToAppState()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (addDialog != null && addDialog?.isShowing == true) {
            addDialog?.dismiss()
        }

        if (infoDialog != null && infoDialog?.isShowing == true) {
            infoDialog?.dismiss()
        }

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val editMenuItem = menu?.findItem(R.id.edit_menu_item)
        when (AppState.mainViewMode) {
            AppState.MainViewMode.normal ->
                editMenuItem?.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_edit_white_24dp))
            AppState.MainViewMode.edit ->
                editMenuItem?.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_done_white_24dp))
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.edit_menu_item -> {
                AppState.toggleMainViewMode()
                true
            }
            R.id.info_menu_item -> {
                openInfoAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun finalize() {
        Timber.i("Activity disposed")
    }

    private fun customizeTitleView() {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val inflater = LayoutInflater.from(this)
        val titleView = inflater.inflate(R.layout.action_bar_title_view, null)
        titleView.title.setText(this.title)
        supportActionBar?.setCustomView(titleView)
    }

    private fun openAddAlertDialog() {
        val builder = AlertDialog.Builder(this)

        if (addDialog != null && addDialog?.isShowing == true) {
            return
        }

        addDialog = builder.create()

        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.activity_add, null, false)

        dialogView.recyclerView.layoutManager = LinearLayoutManager(this)
        dialogView.recyclerView.adapter = AddRecyclerViewAdapter()
        dialogView.addCloseButton.setOnClickListener {
            addDialog?.dismiss()
        }
        addDialog?.setView(dialogView)
        addDialog?.show()
    }

    private fun openInfoAlertDialog() {
        val builder = AlertDialog.Builder(this)

        if (infoDialog != null && infoDialog?.isShowing == true) {
            return
        }

        infoDialog = builder.create()

        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.activity_info, null, false)

        dialogView.infoCloseButton.setOnClickListener {
            infoDialog?.dismiss()
        }
        infoDialog?.setView(dialogView)
        infoDialog?.show()
    }

    private fun subscribeToAppState() {
        disposable += AppState.mainViewModeSubject.subscribe {
            invalidateOptionsMenu()
        }
    }
}
