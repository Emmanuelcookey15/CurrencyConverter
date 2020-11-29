package com.cookey.emmanuel.currencyconverter.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    /**
     * Business
     * */
    @GET("latest")
    fun getCurrencyRate(@Query("access_key") accessToken: String, @Query("symbols") symbol: String): Call<JsonObject>

    @GET("{date}")
    fun getCurrencyHistory(@Path("date") date: String, @Query("access_key") accessToken: String, @Query("symbols") symbol: String): Call<JsonObject>


}