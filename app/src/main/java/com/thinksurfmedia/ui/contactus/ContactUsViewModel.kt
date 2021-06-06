package com.thinksurfmedia.ui.contactus

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thinksurfmedia.backend.Repository


class ContactUsViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    fun submitForm(details: HashMap<String, String>) = repository.submitForm(details).asLiveData()

}