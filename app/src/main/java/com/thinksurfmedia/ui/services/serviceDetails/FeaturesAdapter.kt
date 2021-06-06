package com.thinksurfmedia.ui.services.serviceDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R

class FeaturesAdapter(
    private val features: List<String>,
) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_features, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.featureName.text = features[position]
    }

    override fun getItemCount(): Int {
        return features.size
    }

    inner class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val featureName: TextView = itemView.findViewById(R.id.featureName)
    }
}
