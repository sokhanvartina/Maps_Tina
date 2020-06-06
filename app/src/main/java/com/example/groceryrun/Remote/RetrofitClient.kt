package com.example.groceryrun.Remote

import com.google.firebase.database.core.operation.OperationSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit:Retrofit?=null

    fun getClient (baseUrll:String):Retrofit{
        if (retrofit==null){
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrll)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}