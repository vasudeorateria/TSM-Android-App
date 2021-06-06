package com.thinksurfmedia.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Portfolio
import com.thinksurfmedia.utils.loadImage
import com.thinksurfmedia.utils.portfolioClickListener

class PortfolioAdapter(
    private val portfolios: List<Portfolio>,
    private val clickListener: portfolioClickListener
) :
    RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val viewHolder = PortfolioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_image, parent, false)
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val portfolio = portfolios[position]
        holder.itemView.setOnClickListener {
            clickListener.onClick(portfolio)
        }
        holder.portfolioImage.loadImage(portfolio.images[0])
    }

    override fun getItemCount(): Int {
        return portfolios.size
    }

    inner class PortfolioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val portfolioImage: ImageView = itemView.findViewById(R.id.imageView)
    }
}