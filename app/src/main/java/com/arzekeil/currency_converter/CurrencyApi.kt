package com.arzekeil.currency_converter

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("/v6/{api_key}/pair/{origin}/{target}/{amount}")
    suspend fun getConversionRate(
        @Path("api_key") api_key: String,
        @Path("origin") origin: String,
        @Path("target") target: String,
        @Path("amount") amount: Double
    ): Response<Result>
}