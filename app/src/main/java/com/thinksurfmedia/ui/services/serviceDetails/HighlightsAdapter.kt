package com.thinksurfmedia.ui.services.serviceDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Highlight
import com.thinksurfmedia.utils.loadImage

class HighlightsAdapter(private val highlights: List<Highlight>) :
    RecyclerView.Adapter<HighlightsAdapter.HighLightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighLightViewHolder {
        return HighLightViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_highlights, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HighLightViewHolder, position: Int) {
        val highlight = highlights[position]

        if (position == 0) {
            holder.serviceAnimation.apply {
                visibility = View.VISIBLE
                setAnimationFromUrl(highlight.animation)
            }
        }

        if (position % 2 == 0) {
            holder.leftImage.loadImage(highlight.image)
            holder.rightImage.visibility = View.GONE
        } else {
            holder.rightImage.loadImage(highlight.image)
            holder.leftImage.visibility = View.GONE
        }
        holder.highlightName.text = highlight.name
        holder.highlightDescription.text = highlight.description
    }

    override fun getItemCount(): Int {
        return highlights.size
    }

    inner class HighLightViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val serviceAnimation: LottieAnimationView = itemview.findViewById(R.id.serviceAnimation)
        val leftImage: ImageView = itemview.findViewById(R.id.leftImage)
        val highlightName: TextView = itemview.findViewById(R.id.highlightName)
        val highlightDescription: TextView = itemview.findViewById(R.id.highlightDescription)
        val rightImage: ImageView = itemview.findViewById(R.id.rightImage)
    }
}