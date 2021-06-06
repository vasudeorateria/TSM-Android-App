package com.thinksurfmedia.ui.services

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thinksurfmedia.backend.Repository

class OurServicesViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    val services = repository.getServices().asLiveData()

}