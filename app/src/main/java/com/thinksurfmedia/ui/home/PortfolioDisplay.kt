package com.thinksurfmedia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import me.relex.circleindicator.CircleIndicator2

class PortfolioDisplay : DialogFragment() {

    private val arguments: PortfolioDisplayArgs by navArgs()
    private lateinit var portfolio_images_rv: RecyclerView
    private lateinit var dot_indicator: CircleIndicator2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_portfolio_display, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        portfolio_images_rv = view.findViewById(R.id.portfolio_images_rv)
        dot_indicator = view.findViewById(R.id.dot_indicator)

        portfolio_images_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = PortfolioImagesAdapter(arguments.portfolio.images)
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(this)
            dot_indicator.attachToRecyclerView(this, pagerSnapHelper)
        }
    }
}