package com.thinksurfmedia.backend

import androidx.room.withTransaction
import com.google.gson.JsonObject
import com.thinksurfmedia.model.*
import com.thinksurfmedia.room.*
import com.thinksurfmedia.utils.Resource
import com.thinksurfmedia.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiEndpoints: ApiEndpoints,
    private val tsmEndpoints: TsmEndpoints,
    private val tsmDatabase: TSMDatabase,
    private val highlightDao: HighlightDao,
    private val planDao: PlanDao,
    private val portfolioDao: PortfolioDao,
    private val reviewDao: ReviewDao,
    private val servicesDao: ServicesDao
) {

    fun getPortfolios(): Flow<Resource<List<Portfolio>>> {
        return networkBoundResource(
            query = { portfolioDao.getPortfolios() },
            fetch = { apiEndpoints.getPortfolios() },
            saveFetchedResult = {
                tsmDatabase.withTransaction {
                    portfolioDao.deletePortfolios()
                    portfolioDao.insertPortfolios(it)
                }
            }
        )
    }

    fun getReviews(): Flow<Resource<List<Review>>> {
        return networkBoundResource(
            query = { reviewDao.getReviews() },
            fetch = { apiEndpoints.getReviews() },
            saveFetchedResult = {
                tsmDatabase.withTransaction {
                    reviewDao.deleteReviews()
                    reviewDao.insertReviews(it)
                }
            }
        )
    }

    fun getServices(): Flow<Resource<List<Services>>> {
        return networkBoundResource(
            query = { servicesDao.getServices() },
            fetch = { apiEndpoints.getServices() },
            saveFetchedResult = {
                tsmDatabase.withTransaction {
                    servicesDao.deleteServices()
                    servicesDao.insertServices(it)
                }
            }
        )
    }

    fun getHighlights(serviceId: Int): Flow<Resource<List<Highlight>>> {
        return networkBoundResource(
            query = { highlightDao.getHighlights(serviceId) },
            fetch = { apiEndpoints.getHighlights(serviceId) },
            saveFetchedResult = {
                tsmDatabase.withTransaction {
                    highlightDao.insertHighlights(it)
                }
            }
        )
    }

    fun getConversionRate(currency: HashMap<String, Double>): Flow<Resource<HashMap<String, Double>>> {
        return networkBoundResource(
            query = { flowOf(currency) },
            fetch = {
                val rates = tsmEndpoints.getConversionRates()["rates"] as JsonObject
                currency.keys.forEach {
                    currency[it] = rates[it].asDouble
                }
                currency
            },
            saveFetchedResult = {}
        )
    }

    fun getPlans(serviceId: Int): Flow<Resource<List<Plan>>> {
        return networkBoundResource(
            query = { planDao.getPlans(serviceId) },
            fetch = { apiEndpoints.getPlans(serviceId) },
            saveFetchedResult = {
                tsmDatabase.withTransaction {
                    planDao.insertPlans(it)
                }
            }
        )
    }

    suspend fun getServiceNameList(): List<String> {
        return apiEndpoints.getServiceNamesList()
    }

    suspend fun getPublishableKey(): String {
        val response = apiEndpoints.getPublishableKey()
        return if (response.isSuccessful) {
            response.body()!!.get("publishableKey").asString
        } else {
            throw Exception("Unable to get publishable key from stripe")
        }
    }

    suspend fun createIntent(paymentIntentParams: HashMap<String, Any>): String {
        val response = apiEndpoints.createPaymentIntent(paymentIntentParams)
        return if (response.isSuccessful) {
            response.body()!!.get("clientSecret").asString
        } else {
            throw Exception("Unable to get publishable key from stripe")
        }

    }

    fun submitForm(details: HashMap<String, String>): Flow<Resource<Boolean?>> {
        return networkBoundResource(
            query = { flowOf(null) },
            fetch = {
                tsmEndpoints.submitForm(details)["success"].asBoolean
            },
            saveFetchedResult = {}
        )
    }

}