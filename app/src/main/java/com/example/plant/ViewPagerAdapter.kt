package com.example.plant

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(tipList: ArrayList<String>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private var item = tipList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.tip.text = item[position]
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.tip_item, parent, false)){

        var tip: TextView = itemView.findViewById(R.id.tipContent)
    }
}