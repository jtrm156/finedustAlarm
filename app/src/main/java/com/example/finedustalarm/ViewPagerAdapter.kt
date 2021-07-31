/*
package com.example.finedustalarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(idolList: ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = idolList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = PagerViewHolder((parent))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.idol.setImageResource(item[position])
    }

    override fun getItemCount(): Int = item.size

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.idol_list_item, parent, false)){

        val idol = itemView.findViewById<Int>()
    }
}
 */