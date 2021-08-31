package com.bootcamp.businesscard.data

import com.bootcamp.businesscard.data.model.BusinessCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BusinessCardRepository(private val dao: BusinessCardDao) {

    fun insert(businessCard: BusinessCard) = runBlocking {
        launch(Dispatchers.IO) {
            dao.insert(businessCard)
        }
    }

    fun getAll() = dao.getAll()

    fun delete(businessCard: BusinessCard) = runBlocking {
        launch(Dispatchers.IO){
            dao.delete(businessCard)
        }
    }
}