package com.bootcamp.businesscard.data

import android.content.Context
import androidx.room.*
import com.bootcamp.businesscard.data.model.BusinessCard

@Database(entities = [BusinessCard::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun businessDao(): BusinessCardDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                "businesscard_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}