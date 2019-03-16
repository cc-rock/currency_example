package com.example.currencies.common.fixer

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "http://data.fixer.io/"

interface FixerApi {

    @GET("api/latest")
    suspend fun latestRates(@Query("access_key") accessKey: String,
                            @Query("base") base: String,
                            @Query("symbols") symbols: String): FixerJsonResponse

    @GET("api/{date}")
    suspend fun historicalRates(@Query("access_key") accessKey: String,
                                @Query("base") base: String,
                                @Query("symbols") symbols: String,
                                @Path("date") date: String): FixerJsonResponse

}