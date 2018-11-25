package com.yfujiki.gradeconverter

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.yfujiki.gradeconverter.Models.GradeSystem
import com.yfujiki.gradeconverter.Models.GradeSystemTable
import com.yfujiki.gradeconverter.Models.LocalPreferences
import kotlinx.android.synthetic.main.action_bar_title_view.view.*

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeApp()

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        customizeTitleView()
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

    private fun initializeApp() {
        GradeSystemTable.init(applicationContext)
        LocalPreferences.init(applicationContext)
        LocalPreferences.setSelectedGradeSystems(
                // ToDo : tailor for countries
                GradeSystemTable.gradeSystemsForCountryCode("US")
        )
        // ToDo : tailor for optimal index
        LocalPreferences.setCurrentIndexes(listOf(0))
    }

    private fun customizeTitleView() {
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val inflater = LayoutInflater.from(this)
        val titleView = inflater.inflate(R.layout.action_bar_title_view, null)
        titleView.title.setText(this.title)
        supportActionBar?.setCustomView(titleView)
    }
}
