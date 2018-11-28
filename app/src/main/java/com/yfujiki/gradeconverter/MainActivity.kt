package com.yfujiki.gradeconverter

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import com.yfujiki.gradeconverter.Views.AddRecyclerViewHolder
import com.yfujiki.gradeconverter.Views.MainRecyclerViewHolder
import kotlinx.android.synthetic.main.action_bar_title_view.view.*
import kotlinx.android.synthetic.main.activity_add.view.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_view_holder.view.*

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
        dialogView.recyclerView.adapter = AddRecyclerViewAdapter()
        dialogView.closeButton.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setView(dialogView)
        dialog?.setCancelable(true)
        dialog?.show()
    }
}

private class AddRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.add_recycler_view_holder, p0, false)
        val viewHolder = AddRecyclerViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        val totalCount = GradeSystemTable.tableSize
        val selectedCount = LocalPreferences.selectedGradeSystems().size
        return totalCount - selectedCount
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = p0 as AddRecyclerViewHolder
        val marginParams = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if(p1 == 0) {
            marginParams.topMargin = 32
            marginParams.bottomMargin = 16
        } else if (p1 == itemCount - 1) {
            marginParams.topMargin = 16
            marginParams.bottomMargin = 32
        } else {
            marginParams.topMargin = 16
            marginParams.bottomMargin = 16
        }
        viewHolder.itemView.layoutParams = marginParams
        viewHolder.itemView.requestLayout()

        val gradeSystem = GradeSystemTable.gradeSystemsToAdd()[p1]
        viewHolder.setGrade(gradeSystem)
    }
}
