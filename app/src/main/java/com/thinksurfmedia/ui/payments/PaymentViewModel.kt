package com.thinksurfmedia.ui.payments

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thinksurfmedia.backend.Repository
import kotlinx.coroutines.launch

class PaymentViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    private val publishableKey: LiveData<String> = MutableLiveData()
    private val servicesList: LiveData<List<String>> = MutableLiveData()
    val clientSecret = MutableLiveData<String?>()

    companion object {
        const val TAG = "PaymentViewModel"
    }

    init {
        getKey()
    }

    fun getKey(): LiveData<String> {
        if (publishableKey.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    (publishableKey as MutableLiveData).value = repository.getPublishableKey()
                } catch (exception: Exception) {
                    Log.e(TAG, "getPublishableKey: ${exception.message}")
                }
            }
        }
        return publishableKey
    }

    fun getServiceNameList(): LiveData<List<String>> {
        if (servicesList.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    (servicesList as MutableLiveData).value = repository.getServiceNameList()
                } catch (exception: Exception) {
                    Log.e(TAG, "getServicesList: ${exception.message}")
                }
            }
        }
        return servicesList
    }

    fun createPaymentIntent(paymentIntentParams: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                clientSecret.value = repository.createIntent(paymentIntentParams)
            } catch (exception: Exception) {
                Log.e(TAG, "getPublishableKey: ${exception.message}")
            }
        }
    }

}
