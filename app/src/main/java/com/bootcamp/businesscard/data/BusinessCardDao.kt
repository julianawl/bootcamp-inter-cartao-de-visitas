package com.bootcamp.businesscard.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bootcamp.businesscard.data.model.BusinessCard

@Dao
interface BusinessCardDao {

    @Query("SELECT * FROM BusinessCard")
    fun getAll(): LiveData<List<BusinessCard>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(businessCard: BusinessCard)

    @Delete
    suspend fun delete(businessCard: BusinessCard)
}