package com.thinksurfmedia.ui.services.serviceDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thinksurfmedia.backend.Repository

class ServiceDetailsViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    fun getHighlights(serviceId: Int) = repository.getHighlights(serviceId).asLiveData()

    fun getPlans(serviceId: Int) = repository.getPlans(serviceId).asLiveData()

    fun getConversionRates(currency: HashMap<String, Double>) =
        repository.getConversionRate(currency).asLiveData()

}