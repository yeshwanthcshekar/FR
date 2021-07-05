package com.fetchrewards.codingexercise.network


import com.fetchrewards.codingexercise.model.Item
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.fetchrewards.codingexercise.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

//Interface to define APIs
interface ItemAPI {

    @GET("/hiring.json")
    fun getItems(): Call<List<Item.ItemItem>>

    //Retrofit Instance Builder
    companion object{
        fun create(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : ItemAPI{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(networkConnectionInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()
            return retrofit.create(ItemAPI::class.java)
        }
    }

}