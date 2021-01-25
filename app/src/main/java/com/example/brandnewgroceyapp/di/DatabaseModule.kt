package com.example.brandnewgroceyapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.brandnewgroceyapp.localdatabase.GroceryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): GroceryDatabase {


        val build = Room.databaseBuilder(
            context,
            GroceryDatabase::class.java,
            "grocery_db"
        ).build()
        return build
    }

    @Singleton
    @Provides
    fun provideDao(db:GroceryDatabase)=
        db.dao()

    
}