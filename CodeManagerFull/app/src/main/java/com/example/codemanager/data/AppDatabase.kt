package com.example.codemanager.data

import android.content.Context
import androidx.room.*

@Database(entities=[CodeEntity::class], version=1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): CodeDao

    companion object {
        fun get(ctx:Context)=
            Room.databaseBuilder(ctx,AppDatabase::class.java,"db").build()
    }
}