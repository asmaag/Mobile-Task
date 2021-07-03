package com.mytask.mobiletask.network

import com.mytask.mobiletask.data.model.GeneralResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET(ApiUrls.GET_DOC)
    fun getDocumentsByQuery(
        @Query("q") q: String?,@Query("page") page: Int?):Call<GeneralResponse>

    @GET(ApiUrls.GET_DOC)
    fun getDocumentsByTitle(
            @Query("title") title: String?,@Query("page") page: Int?):Call<GeneralResponse>

    @GET(ApiUrls.GET_DOC)
    fun getDocumentsByAuthor(
            @Query("author") author: String?,@Query("page") page: Int?):Call<GeneralResponse>

    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                        .baseUrl(ApiUrls.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                retrofitService = retrofit.create(RetrofitService::class.java)

            }
            return retrofitService!!
        }
    }
}