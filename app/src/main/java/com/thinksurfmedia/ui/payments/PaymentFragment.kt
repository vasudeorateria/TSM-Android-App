package com.thinksurfmedia.ui.payments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.braintreepayments.api.models.GooglePaymentRequest
import com.google.android.gms.wallet.TransactionInfo
import com.google.android.gms.wallet.WalletConstants
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardInputWidget
import com.thinksurfmedia.R
import com.thinksurfmedia.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    companion object {
        private const val clientToken = "sandbox_ndcm6y7r_2kx46d3tx34dk6xt"
    }

    private lateinit var connection: ConnectionLiveData

    private val paymentViewModel: PaymentViewModel by viewModels()

    private lateinit var payment_amount: TextInputEditText
    private lateinit var cancel: TextView
    private lateinit var serviceName: Spinner
    private lateinit var pay_now: Button
    private lateinit var cardInputWidget: CardInputWidget
    private lateinit var text_input_layout: TextInputLayout
    private lateinit var cardHolderName: TextInputEditText

    private lateinit var paypalCard: MaterialCardView
    private lateinit var stripeCard: MaterialCardView

    val serviceList = mutableListOf(
        "Select Service Name * "
    )
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var stripe: Stripe

    private val dropInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == Activity.RESULT_OK && data != null) {
                val dropInResult =
                    data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT) as DropInResult?
                if (dropInResult != null) {
                    val nNonce = dropInResult.paymentMethodNonce!!.nonce
                    val paymentType = dropInResult.paymentMethodType!!.canonicalName
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
//                    postNonceToYourServer(nNonce, payment_type, user_id)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                print("Payment cancelled by user, go back to previous activity")
            } else {
                val error = data?.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
                print("Get some unknown error, go back to previous activity")
            }
        }

    private val dropInRequest by lazy {
        DropInRequest()
            .tokenizationKey(clientToken)
            .collectDeviceData(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        connection = ConnectionLiveData(requireContext())

        payment_amount = view.findViewById(R.id.payment_amount)
        cancel = view.findViewById(R.id.cancel)
        serviceName = view.findViewById(R.id.service_name)
        pay_now = view.findViewById(R.id.pay_now)
        paypalCard = view.findViewById(R.id.paypal)
        stripeCard = view.findViewById(R.id.stripe)
        cardInputWidget = view.findViewById(R.id.card_widget)
        text_input_layout = view.findViewById(R.id.text_input_layout)
        cardHolderName = view.findViewById(R.id.name_on_card)

        payment_amount.doAfterTextChanged {
            setButtonState()
        }

        cancel.setOnClickListener { restoreLayout() }

        setServicesNameList()

        paymentViewModel.getServiceNameList().observe(viewLifecycleOwner, Observer { services ->
            spinnerAdapter.addAll(services)
            spinnerAdapter.notifyDataSetChanged()
        })

        paypalCard.setOnClickListener {
            paypalCard.isChecked = !paypalCard.isChecked
            stripeCard.isChecked = false
            displayCardWidget()
            setButtonState()
        }

        stripeCard.apply {

            setOnClickListener {
                this.isChecked = !this.isChecked
                paypalCard.isChecked = false
            }

            setOnCheckedChangeListener { card, isChecked ->
                displayCardWidget()
                setButtonState()
            }

        }

        cardInputWidget.postalCodeEnabled = false

        cardHolderName.doAfterTextChanged {
            setButtonState()
        }

        pay_now.setOnClickListener {
            pay_now.disabled()
            if (stripeCard.isChecked) {
                stripePayment()
            } else {
                payPalPayment()
            }
        }

        paymentViewModel.getKey().observe(viewLifecycleOwner, Observer { publishableKey ->
            PaymentConfiguration.init(requireContext(), publishableKey)
            stripe = Stripe(requireContext(), publishableKey)
        })

    }

    private fun setServicesNameList() {
        serviceName.apply {

            spinnerAdapter = object : ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                serviceList
            ) {

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val textView: TextView =
                        super.getDropDownView(position, convertView, parent) as TextView

                    // set selected item style
                    if (position == serviceName.selectedItemPosition && position != 0) {
                        val background = ColorDrawable(context.getColor(R.color.dark_blue))
                        background.alpha = 50
                        textView.background = background
                    }

                    // make hint item color gray
                    if (position == 0) {
                        textView.setTextColor(Color.GRAY)
                    }

                    return textView
                }

                override fun isEnabled(position: Int): Boolean {
                    // disable first item
                    // first item is display as hint
                    return position != 0
                }
            }

            adapter = spinnerAdapter

            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View, position: Int, id: Long
                ) {
                    setButtonState()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

        }

    }

    private fun restoreLayout() {
        payment_amount.text = null
        serviceName.setSelection(0)
        paypalCard.isChecked = false
        stripeCard.isChecked = false
        cardInputWidget.clear()
        cardHolderName.text = null
    }

    private fun stripePayment() {

        displayProgressDialog(context)

        // create payment intent
        val paymentIntentParams = HashMap<String, Any>()
        val description =
            "Payment for ${serviceName.selectedItem} by ${cardHolderName.text.toString()}"
        paymentIntentParams.apply {
            put("amount", payment_amount.text.toString().toInt() * 100)
            put("currency", "usd")
            put("description", description)
        }
        paymentViewModel.createPaymentIntent(paymentIntentParams)

        paymentViewModel.clientSecret.observe(viewLifecycleOwner, Observer { clientSecret ->
            if (clientSecret != null) {
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(
                        cardInputWidget.paymentMethodCreateParams!!,
                        clientSecret
                    )
                paymentViewModel.clientSecret.value = null
                stripe.confirmPayment(this, confirmParams)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {

            override fun onError(e: Exception) {
                failedDialog(context, e.message.toString())
                pay_now.enabled()
            }

            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                if (paymentIntent.status == StripeIntent.Status.Succeeded) {
                    successDialog(context, "Payment completed successfully")
                } else if (paymentIntent.status == StripeIntent.Status.RequiresPaymentMethod) {
                    failedDialog(context, "Unable to process payment. Payment method required ")
                }
                pay_now.enabled()
            }

        })
    }

    private fun payPalPayment() {
        enableGooglePay(dropInRequest)
        dropInResult.launch(dropInRequest.getIntent(context))
    }

    private fun setButtonState() {
        pay_now.disabled()
        cancel.disabled()

        val amountState = !payment_amount.text.isNullOrBlank()
        val spinnerState = serviceName.selectedItemPosition != 0
        val gatewayState = paypalCard.isChecked || stripeCard.isChecked
        val cardNameState = stripeCard.isChecked && cardHolderName.text?.length!! >= 3
        val stripeState = this::stripe.isInitialized

        val cancelState = amountState || spinnerState || gatewayState || cardNameState
        var payNowState =
            amountState && spinnerState && gatewayState && cardNameState && stripeState

        if (payNowState) {
            val cardWidgetParams = cardInputWidget.paymentMethodCreateParams != null
            payNowState = payNowState && cardWidgetParams
        }

        val connectionState = connection.value == true
        if (connectionState && payNowState) {
            pay_now.enabled()
        }

        connection.observe(viewLifecycleOwner, Observer { networkState ->
            if (connectionState != networkState && payNowState) {
                setButtonState()
            }
        })

        if (cancelState) {
            cancel.enabled()
        }

    }

    private fun displayCardWidget() {
        if (stripeCard.isChecked) {
            cardInputWidget.visibility = View.VISIBLE
            text_input_layout.visibility = View.VISIBLE
        } else {
            cardInputWidget.visibility = View.GONE
            text_input_layout.visibility = View.GONE
        }
    }

    private fun enableGooglePay(dropInRequest: DropInRequest) {
        val googlePaymentRequest = GooglePaymentRequest()
            .transactionInfo(
                TransactionInfo.newBuilder()
                    .setTotalPrice(payment_amount.text.toString())
                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                    .setCurrencyCode("USD")
                    .build()
            ).googleMerchantId("merchant-id-from-google")

        dropInRequest.googlePaymentRequest(googlePaymentRequest)
    }

}


