package com.rmohd8788.helloji.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.model.Trending
import kotlinx.android.synthetic.main.trending_list.view.*

class TrendingAdapter(private val trendingList : List<Trending>) : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.trending_list,parent,false))
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val listPosition = trendingList[position]

        holder.itemView.tvTrendingTags.text = listPosition.tags
        holder.itemView.tvNearbyPeople.text = listPosition.nearbyPeople
    }

    override fun getItemCount() = trendingList.size

    class TrendingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

}