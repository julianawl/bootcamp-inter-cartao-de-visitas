package com.bootcamp.businesscard

import android.app.Application
import com.bootcamp.businesscard.data.AppDatabase
import com.bootcamp.businesscard.data.BusinessCardRepository

class App: Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BusinessCardRepository(database.businessDao()) }
}