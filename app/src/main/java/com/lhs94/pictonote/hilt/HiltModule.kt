package com.lhs94.pictonote.hilt

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lhs94.pictonote.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {
    @Provides
    fun appDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database").enableMultiInstanceInvalidation().build()
    }
}