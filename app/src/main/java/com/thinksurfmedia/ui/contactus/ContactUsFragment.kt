package com.thinksurfmedia.ui.contactus

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.hbb20.CountryCodePicker
import com.thinksurfmedia.R
import com.thinksurfmedia.utils.Resource
import com.thinksurfmedia.utils.displayProgressDialog
import com.thinksurfmedia.utils.failedDialog
import com.thinksurfmedia.utils.successDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class ContactUsFragment : Fragment(R.layout.fragment_contact_us) {

    private val contactUsViewModel: ContactUsViewModel by viewModels()

    private lateinit var name: TextInputEditText
    private lateinit var business_name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var countryCode: CountryCodePicker
    private lateinit var phone: TextInputEditText
    private lateinit var message: TextInputEditText
    private lateinit var submit: Button

    private var phoneNumber = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        name = view.findViewById(R.id.name)
        business_name = view.findViewById(R.id.business_name)
        email = view.findViewById(R.id.email)
        countryCode = view.findViewById(R.id.countryCode)
        phone = view.findViewById(R.id.phone)
        message = view.findViewById(R.id.message)
        submit = view.findViewById(R.id.submit)

        name.doAfterTextChanged {
            setButtonState(false)
        }
        business_name.doAfterTextChanged {
            setButtonState(false)
        }
        email.doAfterTextChanged {
            setButtonState(false)
        }
        phone.doAfterTextChanged {
            setButtonState(false)
        }
        message.doAfterTextChanged {
            setButtonState(false)
        }

        submit.setOnClickListener {
            if (setButtonState(true)) {
                val details = hashMapOf(
                    Pair("name", name.text.toString()),
                    Pair("business_name", business_name.text.toString()),
                    Pair("email", email.text.toString()),
                    Pair("phone", phoneNumber),
                    Pair("message", message.text.toString())
                )

                contactUsViewModel.submitForm(details)
                    .observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Success -> {
                                successDialog(context, getString(R.string.formSubmit_success))
                            }
                            is Resource.Error -> failedDialog(
                                context,
                                getString(R.string.formSubmit_failed)
                            )
                            is Resource.Loading -> {
                                displayProgressDialog(context)
                            }
                        }
                    }
            }
        }
    }

    private fun setButtonState(shoeError: Boolean): Boolean {

        val nameState = name.text?.length!! >= 3
        if (!nameState) {
            if (shoeError) name.error = getString(R.string.min_3_character)
            return false
        }

        val businessNameState = business_name.text?.length!! >= 3
        if (!businessNameState) {
            if (shoeError) business_name.error = getString(R.string.min_3_character)
            return false
        }

        val emailState = Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), email.text.toString())
        if (!emailState) {
            if (shoeError) email.error = getString(R.string.valid_email)
            return false
        }

        val phoneState = phone.text?.length in 6..13
        if (!phoneState) {
            if (shoeError) phone.error = getString(R.string.valid_phone)
            return false
        }
        phoneNumber = "+${countryCode.selectedCountryCode} ${phone.text}"

        val messageState = message.text?.length!! >= 10
        if (!messageState) {
            if (shoeError) message.error = getString(R.string.min_10_character)
            return false
        }

        return true

    }

}