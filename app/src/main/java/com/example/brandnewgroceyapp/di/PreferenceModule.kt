package com.example.brandnewgroceyapp.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PreferenceModule {

    @Singleton
    @Provides
    fun providePreference(@ApplicationContext context: Context):SharedPreferences {
       return context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePreferenceEditor(sharedPreferences: SharedPreferences):SharedPreferences.Editor {
      return sharedPreferences.edit()
    }


}