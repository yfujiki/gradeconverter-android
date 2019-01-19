package com.responsivebytes.gradeconverter.Dagger

import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModuleUITest {
    @Provides
    @Singleton
    fun provideLocalPreference(app: GCApp): LocalPreferences {
        return LocalPreferencesImpl(app, "UITest")
    }

    @Provides
    @Singleton
    fun provideAppState(): AppState {
        return AppStateTestingImpl()
    }
}
