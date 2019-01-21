package com.responsivebytes.gradeconverter.Dagger

import com.responsivebytes.gradeconverter.GCApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityModule::class])
interface AppComponent : AndroidInjector<GCApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GCApp>()
}
