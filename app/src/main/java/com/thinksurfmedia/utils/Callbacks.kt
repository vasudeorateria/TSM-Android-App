package com.thinksurfmedia.utils

import com.thinksurfmedia.model.Portfolio
import com.thinksurfmedia.model.Services

interface serviceClickListener {
    fun onClick(service: Services)
}

interface rvItemClickListener {
    fun onClick()
}

interface portfolioClickListener {
    fun onClick(portfolio: Portfolio)
}