package com.bootcamp.businesscard.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.businesscard.data.model.BusinessCard

class BusinessCardAdapter(val cards: MutableList<BusinessCard> = mutableListOf()): RecyclerView.Adapter<BusinessCardAdapter.BusinessCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessCardViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BusinessCardViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = cards.size

    inner class BusinessCardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
}