package com.responsivebytes.gradeconverter.Dagger

import com.responsivebytes.gradeconverter.GCApp
import com.responsivebytes.gradeconverter.Models.AppState
import com.responsivebytes.gradeconverter.Models.AppStateImpl
import com.responsivebytes.gradeconverter.Models.LocalPreferences
import com.responsivebytes.gradeconverter.Models.LocalPreferencesImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideLocalPreference(app: GCApp): LocalPreferences {
        return LocalPreferencesImpl(app)
    }

    @Provides
    @Singleton
    fun provideAppState(): AppState {
        return AppStateImpl()
    }
}
