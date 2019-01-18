package com.responsivebytes.gradeconverter.Dagger

import com.responsivebytes.gradeconverter.AddDialogFragment
import com.responsivebytes.gradeconverter.MainActivityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivityFragment(): MainActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeAddDialogFragment(): AddDialogFragment
}
