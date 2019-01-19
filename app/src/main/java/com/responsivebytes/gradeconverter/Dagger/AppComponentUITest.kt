package com.responsivebytes.gradeconverter.Dagger

import com.responsivebytes.gradeconverter.Adapters.AddRecyclerViewAdapter
import com.responsivebytes.gradeconverter.GCApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModuleUITest::class, ActivityModule::class])
interface AppComponentUITest : AndroidInjector<GCApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GCApp>()

    fun inject(viewAdapter: AddRecyclerViewAdapter)
}
