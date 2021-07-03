package com.mytask.mobiletask.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytask.mobiletask.data.model.Document
import com.mytask.mobiletask.data.listenners.OnItemClickListener
import com.mytask.mobiletask.databinding.ItemDocumentBinding
import com.mytask.mobiletask.viewholder.DocumentViewHolder

class DocumentsAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DocumentViewHolder>() {

    var documents = mutableListOf<Document>()

    fun setDocumentsList(documents: List<Document>) {
        this.documents = documents.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return    DocumentViewHolder(
            binding,onItemClickListener
        )
    }


    override fun getItemCount(): Int {
        return documents.size
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        holder.bind(document)
    }
}