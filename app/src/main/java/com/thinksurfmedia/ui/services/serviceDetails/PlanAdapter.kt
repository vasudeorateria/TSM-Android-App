package com.thinksurfmedia.ui.services.serviceDetails

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Plan
import com.thinksurfmedia.utils.rvItemClickListener
import kotlin.math.round

class PlanAdapter(
    private val plans: List<Plan>,
    private val rvItemClickListener: rvItemClickListener
) :
    RecyclerView.Adapter<PlanAdapter.PricingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricingViewHolder {
        return PricingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_plan_details, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PricingViewHolder, position: Int) {
        val plan = plans[position]

        holder.planLayout.setBackgroundColor(Color.parseColor(plan.color))
        holder.planName.text = plan.name

        holder.planFeatures.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FeaturesAdapter(plan.features)
        }
        holder.getStarted.setOnClickListener {
            rvItemClickListener.onClick()
        }

        plan.price.apply {
            val startIndex = indexOf(' ')
            val endIndex = when (contains('/')) {
                true -> indexOf('/')
                false -> length
            }
            val price = substring(startIndex, endIndex).toDouble()
            holder.planPrice.text = replace("$price", "${round(round(price)).toInt()}")
        }
    }

    override fun getItemCount(): Int {
        return plans.size
    }

    inner class PricingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planLayout: LinearLayout = itemView.findViewById(R.id.planLayout)
        val planName: TextView = itemView.findViewById(R.id.planName)
        val planFeatures: RecyclerView = itemView.findViewById(R.id.planFeatures)
        val planPrice: TextView = itemView.findViewById(R.id.planPrice)
        val getStarted: Button = itemView.findViewById(R.id.getStarted)
    }
}
