
package com.example.finedustalarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.finedustalarm.databinding.IdolListItemBinding

class ViewPagerAdapter(idolList: ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = idolList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = PagerViewHolder((parent))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.idol.setImageResource(item[position%3])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.idol_list_item, parent, false)){

        val idol = itemView.findViewById<AppCompatImageView>(R.id.imageView_idol)
    }
}
