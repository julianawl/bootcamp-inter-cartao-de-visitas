package com.bootcamp.businesscard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusinessCard(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val company: String,
    val background: String,
    val profilePic: String
)
