package com.thinksurfmedia.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.utils.loadImage

class PortfolioImagesAdapter(private val portfolioImages: List<String>) :
    RecyclerView.Adapter<PortfolioImagesAdapter.PortfolioImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioImageViewHolder {
        val viewHolder = PortfolioImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_image, parent, false)
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: PortfolioImageViewHolder, position: Int) {
        val portfolioImage = portfolioImages[position]
        holder.portfolioImage.loadImage(portfolioImage)
    }

    override fun getItemCount(): Int {
        return portfolioImages.size
    }

    inner class PortfolioImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val portfolioImage: ImageView = itemView.findViewById(R.id.imageView)
    }
}