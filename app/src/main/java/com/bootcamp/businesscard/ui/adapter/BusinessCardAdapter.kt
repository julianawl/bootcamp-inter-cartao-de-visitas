package com.bootcamp.businesscard.ui.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.businesscard.data.model.BusinessCard
import com.bootcamp.businesscard.databinding.ItemBusinessCardBinding

class BusinessCardAdapter: ListAdapter<BusinessCard, BusinessCardAdapter.BusinessCardViewHolder>(DiffCallback()) {

    var deleteClickListener: (businessCard: BusinessCard) -> Unit = {}
    var shareClickListener: (View) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBusinessCardBinding.inflate(inflater, parent, false)
        return BusinessCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusinessCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BusinessCardViewHolder(private val binding: ItemBusinessCardBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: BusinessCard){
            binding.itemName.text = item.name
            binding.itemPhone.text = item.phone
            binding.itemEmail.text = item.email
            binding.itemCompanyName.text = item.company
            binding.itemMcv.setCardBackgroundColor(Color.parseColor(item.background))
            binding.shapeableImageView.setImageURI(Uri.parse(item.profilePic))
            binding.itemMcv.setOnClickListener {
                shareClickListener(it)
            }
            binding.itemDeleteBtn.setOnClickListener {
                deleteClickListener(item)
            }
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<BusinessCard>(){
    override fun areItemsTheSame(oldItem: BusinessCard, newItem: BusinessCard
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: BusinessCard, newItem: BusinessCard
    ): Boolean = oldItem.id == newItem.id

}