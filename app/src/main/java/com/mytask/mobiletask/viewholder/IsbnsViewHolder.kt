package com.mytask.mobiletask.viewholder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mytask.mobiletask.R
import com.mytask.mobiletask.databinding.ItemIsbnBinding


class IsbnsViewHolder (val binding: ItemIsbnBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(isbn: String) {
        binding.tvTitleIsbn.text = isbn
            Glide.with(itemView.context)
                .load("http://covers.openlibrary.org/b/isbn/"+isbn+"-M.jpg?default=false")
                .error(R.drawable.default_image_placeholder)
                .placeholder(R.drawable.default_image_placeholder)
                .into(binding.imageViewIsbn)
        }
    }
