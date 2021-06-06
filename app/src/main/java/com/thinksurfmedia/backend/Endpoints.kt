package com.thinksurfmedia.backend

import com.google.gson.JsonObject
import com.thinksurfmedia.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiEndpoints {

    @GET("portfolios")
    suspend fun getPortfolios(): List<Portfolio>

    @GET("reviews")
    suspend fun getReviews(): List<Review>

    @GET("payments/service-names-list")
    suspend fun getServiceNamesList(): List<String>

    @GET("payments/config")
    suspend fun getPublishableKey(): Response<JsonObject>

    @POST("payments/create-payment-intent")
    suspend fun createPaymentIntent(@Body paymentIntentParams: HashMap<String, Any>): Response<JsonObject>

    @GET("services")
    suspend fun getServices(): List<Services>

    @GET("services/{serviceId}/highlights")
    suspend fun getHighlights(@Path("serviceId") serviceId: Int): List<Highlight>

    @GET("services/{serviceId}/plans")
    suspend fun getPlans(@Path("serviceId") serviceId: Int): List<Plan>

}

interface TsmEndpoints {

    @GET("tsm/currencies")
    suspend fun getConversionRates(): JsonObject

    @POST("jet-cct/android_app_form_sub")
    suspend fun submitForm(@Body details: HashMap<String, String>): JsonObject

}