package com.thinksurfmedia.ui.services

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thinksurfmedia.R
import com.thinksurfmedia.model.Services
import com.thinksurfmedia.utils.serviceClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OurServicesFragment : Fragment(R.layout.fragment_services) {

    private val ourServicesViewModel: OurServicesViewModel by viewModels()
    private lateinit var services_list: RecyclerView
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        services_list = view.findViewById(R.id.services_list)
        ourServicesViewModel.services.observe(viewLifecycleOwner) {
            if (it.data != null) {
                setServices(it.data)
            }
        }
    }

    private fun setServices(servicesList: List<Services>) {
        services_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ServicesAdapter(servicesList, object : serviceClickListener {
                override fun onClick(service: Services) {
                    val action =
                        OurServicesFragmentDirections.actionServicesToServiceDetailsFragment(
                            service.name,
                            service.id
                        )
                    navController.navigate(action)
                }
            })
        }
    }

}