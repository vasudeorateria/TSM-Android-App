package com.thinksurfmedia.ui.services.serviceDetails

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Highlight
import com.thinksurfmedia.model.Plan
import com.thinksurfmedia.utils.CustomLinearLayoutManager
import com.thinksurfmedia.utils.rvItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator2
import java.util.*

@AndroidEntryPoint
class ServiceDetailsFragment : Fragment(R.layout.fragment_service_details) {

    private val serviceDetailViewModel: ServiceDetailsViewModel by viewModels()

    var currency = hashMapOf(
        Pair("USD", 1.0),
        Pair("CAD", 1.0),
        Pair("GBP", 1.0),
        Pair("AUD", 1.0),
        Pair("EUR", 1.0),
        Pair("INR", 1.0)
    )

    private lateinit var highlights_rv: RecyclerView
    private lateinit var currency_list: Spinner
    private lateinit var pricing_rv: RecyclerView
    private lateinit var dot_indicator: CircleIndicator2

    private lateinit var navController: NavController

    private var selectedCurrency = ""
    private var planList = mutableListOf<Plan>()

    private lateinit var planAdapter: PlanAdapter

    private val arguments: ServiceDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        highlights_rv = view.findViewById(R.id.highlights_rv)
        currency_list = view.findViewById(R.id.currency_list)
        pricing_rv = view.findViewById(R.id.plans_rv)
        dot_indicator = view.findViewById(R.id.dot_indicator)

        navController = findNavController()

        currency_list.apply {
            val currencies = currency.keys.toList()
            adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    currencies
                )
            selectedCurrency = currencies[0]

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View, position: Int, id: Long
                ) {
                    if (selectedCurrency != selectedItem.toString()) {
                        setPrice(selectedCurrency, selectedItem.toString())
                        selectedCurrency = selectedItem.toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        }

        val serviceId = arguments.serviceId
        serviceDetailViewModel.getHighlights(serviceId).observe(viewLifecycleOwner) {
            if (it.data != null) {
                setHighLights(it.data)
            }
        }

        serviceDetailViewModel.getPlans(serviceId).observe(viewLifecycleOwner) {
            if (it.data != null) {
                planList.addAll(it.data)
                setPlans(it.data)
            }
        }

        serviceDetailViewModel.getConversionRates(currency).observe(viewLifecycleOwner) {
            if (it.data != null) {
                currency = it.data
            }
        }

    }

    private fun setHighLights(highlightsList: List<Highlight>) {
        highlights_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HighlightsAdapter(highlightsList)
        }
    }

    private fun setPrice(fromCurrency: String, toCurrency: String) {

        val fromRate = currency[fromCurrency]!!
        val fromLocale = Locale("en", fromCurrency)
        val fromCurrencySymbol = Currency.getInstance(fromCurrency).getSymbol(fromLocale)

        val toRate = currency[toCurrency]!!
        val toLocale = Locale("en", toCurrency)
        val toCurrencySymbol = Currency.getInstance(toCurrency).getSymbol(toLocale)

        val finalRate = toRate / fromRate
        planList.forEach { plan ->

            plan.apply {
                val startIndex = price.indexOf(' ')
                val endIndex = when (price.contains('/')) {
                    true -> price.indexOf('/')
                    false -> price.length
                }
                val oldPrice = price.substring(startIndex, endIndex).toDouble()
                // todo change data in database and remove this line
                if (!price.contains('.')) {
                    plan.price = price.replace("${oldPrice.toInt()}", "$oldPrice")
                }
                val newPrice = oldPrice * finalRate
                plan.price = price.replace("$oldPrice", "$newPrice")
                    .replace(fromCurrencySymbol, toCurrencySymbol)
            }
        }
        planAdapter.notifyDataSetChanged()

    }

    private fun setPlans(plansList: List<Plan>) {
        pricing_rv.apply {
            layoutManager = CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
            planAdapter = PlanAdapter(
                plansList,
                object : rvItemClickListener {
                    override fun onClick() {
                        navController.apply {
                            popBackStack(R.id.services, true)
                            navigate(R.id.contact_us)
                        }
                    }
                })
            adapter = planAdapter
            val pagerSnapHelper = LinearSnapHelper()
            dot_indicator.attachToRecyclerView(this, pagerSnapHelper)
        }
    }

}