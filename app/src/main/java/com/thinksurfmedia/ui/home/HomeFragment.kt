package com.thinksurfmedia.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Portfolio
import com.thinksurfmedia.model.Review
import com.thinksurfmedia.utils.portfolioClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator2

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var scrollView: NestedScrollView
    private lateinit var getStarted: Button
    private lateinit var portfolio_rv: RecyclerView
    private lateinit var clients_rv: RecyclerView
    private lateinit var contact_us: Button
    private lateinit var reviews_rv: RecyclerView
    private lateinit var dot_indicator: CircleIndicator2
    private lateinit var payments: TextView

    private lateinit var navController: NavController


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        scrollView = view.findViewById(R.id.scrollView)
        getStarted = view.findViewById(R.id.getStarted)
        portfolio_rv = view.findViewById(R.id.portfolio_rv)
        clients_rv = view.findViewById(R.id.client_rv)
        contact_us = view.findViewById(R.id.contact_us)
        reviews_rv = view.findViewById(R.id.reviews_rv)
        dot_indicator = view.findViewById(R.id.dot_indicator)
        payments = view.findViewById(R.id.payments)

        navController = findNavController()

        getStarted.setOnClickListener {
            navController.navigate(R.id.contact_us)
        }

        contact_us.setOnClickListener {
            navController.navigate(R.id.contact_us)
        }

        homeViewModel.portfolios.observe(viewLifecycleOwner) {
            if (it.data != null) {
                setPortfolios(it.data)
            }
        }

        homeViewModel.reviews.observe(viewLifecycleOwner) {
            if (it.data != null) {
                setReviews(it.data)
            }
        }

        payments.setOnClickListener {
            navController.navigate(R.id.paymentFragment)
        }

    }

    private fun setPortfolios(portfolioList: List<Portfolio>) {
        portfolio_rv.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = PortfolioAdapter(portfolioList, object : portfolioClickListener {
                override fun onClick(portfolio: Portfolio) {
                    val action = HomeFragmentDirections.actionNavigationHomeToAddToDoFragment(
                        portfolio
                    )
                    navController.navigate(action)
                }
            })

        }
    }

    private fun setReviews(reviewList: List<Review>) {
        reviews_rv.apply {
            val rv_layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = rv_layoutManager
            adapter = ReviewAdapter(reviewList)

            val pagerSnapHelper = LinearSnapHelper()
            dot_indicator.attachToRecyclerView(this, pagerSnapHelper)
            autoScroll(reviewList)
        }
    }

    private fun autoScroll(reviewList: List<Review>) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            delay(10000)
            val layoutManager = reviews_rv.layoutManager as LinearLayoutManager
            val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
            if (lastVisible < reviewList.size - 1) {
                layoutManager.smoothScrollToPosition(
                    reviews_rv,
                    RecyclerView.State(),
                    lastVisible + 1
                )
            } else {
                layoutManager.smoothScrollToPosition(reviews_rv, RecyclerView.State(), 0)
            }
            autoScroll(reviewList)
        }
    }

}