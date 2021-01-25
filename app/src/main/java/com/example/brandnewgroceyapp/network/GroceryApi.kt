package com.example.brandnewgroceyapp.network

import com.example.brandnewgroceyapp.model.Groceries
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GroceryApi {

    @GET("get_all.php")
    suspend fun getAllGroceries(): Response<Groceries>

    @FormUrlEncoded
    @POST("get_by_category.php")
    suspend fun getGroceryByCategory(
        @Field("category") category: String
    ): Response<Groceries>

    @FormUrlEncoded
    @POST("get_item_by_name.php")
    suspend fun get_by_name(
        @Field("name") name:String
    ):Response<Groceries>


}