package com.thinksurfmedia.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Services
import com.thinksurfmedia.utils.loadImage
import com.thinksurfmedia.utils.serviceClickListener

class ServicesAdapter(
    private val servicesList: List<Services>,
    private val serviceClickListener: serviceClickListener
) : RecyclerView.Adapter<ServicesAdapter.ServicesListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesListViewHolder {
        val viewHolder = ServicesListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_services, parent, false)
        )
        viewHolder.itemView.setOnClickListener {
            serviceClickListener.onClick(servicesList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ServicesListViewHolder, position: Int) {
        val service = servicesList[position]

        holder.name.text = service.name
        holder.image.loadImage(service.image)
        holder.description.text = service.description
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

    inner class ServicesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.service_name)
        val image: ImageView = itemView.findViewById(R.id.service_image)
        val description: TextView = itemView.findViewById(R.id.service_description)
    }
}
