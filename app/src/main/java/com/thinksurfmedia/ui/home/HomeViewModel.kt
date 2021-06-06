package com.thinksurfmedia.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.thinksurfmedia.backend.Repository


class HomeViewModel @ViewModelInject constructor(private val repository: Repository) : ViewModel() {

    val portfolios = repository.getPortfolios().asLiveData()

    val reviews = repository.getReviews().asLiveData()

}