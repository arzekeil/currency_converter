package com.arzekeil.currency_converter

import retrofit2.Response
import retrofit2.http.GET

interface Unit {
    @GET("/")
    suspend fun getAvailableCurrencies(): Response<List<Unit>>
}