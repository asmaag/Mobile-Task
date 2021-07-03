package com.mytask.mobiletask.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mytask.mobiletask.data.model.Document
import com.mytask.mobiletask.data.listenners.OnItemClickListener
import com.mytask.mobiletask.databinding.ItemDocumentBinding


class DocumentViewHolder(val binding: ItemDocumentBinding,onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var document: Document

    init {

    itemView.setOnClickListener {
        onItemClickListener.onCickItem(document)
    }
}
    fun bind(document: Document) {
        this.document = document

        val authersNames= if(document.authors != null) "by "+ document.authors.joinToString(separator = " , ")else ""
        binding.tvTitle.text = document.title
        binding.tvAuthorName.text = authersNames

        }
    }
