package com.mytask.mobiletask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mytask.mobiletask.databinding.ItemIsbnBinding
import com.mytask.mobiletask.viewholder.IsbnsViewHolder

class IspnAdapter(
) : RecyclerView.Adapter<IsbnsViewHolder>() {

    var isbns = mutableListOf<String>()
    fun setIsbnsList(isbns: List<String>) {
        this.isbns = isbns.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IsbnsViewHolder {
        val binding = ItemIsbnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return    IsbnsViewHolder(
                binding
        )
    }

/*
   to return max 5 result only
 */
    override fun getItemCount(): Int {
        if (isbns.size > 5){
            return 5
        }
        return isbns.size
    }

    override fun onBindViewHolder(holder: IsbnsViewHolder, position: Int) {
        val isbn = isbns[position]
        holder.bind(isbn)
    }
}