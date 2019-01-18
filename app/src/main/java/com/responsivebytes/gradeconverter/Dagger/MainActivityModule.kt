package com.responsivebytes.gradeconverter.Dagger

import com.responsivebytes.gradeconverter.MainActivityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivityFragment(): MainActivityFragment
}
