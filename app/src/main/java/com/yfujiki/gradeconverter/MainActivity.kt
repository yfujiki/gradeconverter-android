package com.yfujiki.gradeconverter

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.yfujiki.gradeconverter.Adapters.AddRecyclerViewAdapter
import kotlinx.android.synthetic.main.action_bar_title_view.view.*
import kotlinx.android.synthetic.main.activity_add.view.*

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            openAddAlertDialog()
        }

        customizeTitleView()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.edit_menu_item -> true
            R.id.info_menu_item -> true
            else -> super.onOptionsItemSelected(item)
        }
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

        if (dialog != null && dialog?.isShowing == true) {
            return
        }

        dialog = builder.create()

        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.activity_add, null, false)

        dialogView.recyclerView.layoutManager = LinearLayoutManager(this)
        dialogView.recyclerView.adapter = AddRecyclerViewAdapter(this)
        dialogView.closeButton.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setView(dialogView)
        dialog?.show()
    }
}
