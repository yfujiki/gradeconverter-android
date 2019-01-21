package com.responsivebytes.gradeconverter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.view.*
import com.responsivebytes.gradeconverter.Models.AppState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.action_bar_title_view.view.*

import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import android.support.v4.app.Fragment
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var appState: AppState

    @Inject
    lateinit var localPreferences: LocalPreferences

    val disposable: CompositeDisposable = CompositeDisposable()

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            openAddAlertDialog()
        }

        showHideFab()

        customizeTitleView()

        subscribeToAppState()
        subscribeToLocalPreference()
    }

    override fun onDestroy() {
        super.onDestroy()

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
        when (appState.mainViewMode) {
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
                appState.toggleMainViewMode()
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
        val addDialogFragment = AddDialogFragment()
        addDialogFragment.show(supportFragmentManager, "Add Dialog")
    }

    private fun openInfoAlertDialog() {
        val infoDialogFragment = InfoDialogFragment()
        infoDialogFragment.show(supportFragmentManager, "Info Dialog")
    }

    private fun subscribeToAppState() {
        disposable += appState.mainViewModeSubject.subscribe {
            invalidateOptionsMenu()
            showHideFab()
        }
    }

    private fun subscribeToLocalPreference() {
        disposable += localPreferences.selectedGradeSystemsChanged
                .subscribe {
                    showHideFab()
        }
    }

    private fun showHideFab() {
        val hasSomethingToAdd = localPreferences.unselectedGradeSystems().count() > 0
        val isNormalMode = appState.mainViewMode == AppState.MainViewMode.normal

        if (hasSomethingToAdd && isNormalMode) {
            fab.show()
        } else {
            fab.hide()
        }
    }
}
