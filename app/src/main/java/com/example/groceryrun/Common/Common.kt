package com.example.groceryrun.Common

import com.example.groceryrun.Remote.IGoogleAPIService
import com.example.groceryrun.Remote.RetrofitClient

object Common {
    private val GOOGLE_API_URL="https://maps.googleapis.com/"
    val googleApiService:IGoogleAPIService
    get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}