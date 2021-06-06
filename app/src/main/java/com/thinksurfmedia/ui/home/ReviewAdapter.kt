package com.thinksurfmedia.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Review
import com.thinksurfmedia.utils.loadImage

class ReviewAdapter(val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout_review,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val positionInList = position % reviews.size
        val review = reviews[positionInList]
        holder.review.text = review.review
        holder.image.loadImage(review.image)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.review_image)
        val review: TextView = itemView.findViewById(R.id.review)
    }
}