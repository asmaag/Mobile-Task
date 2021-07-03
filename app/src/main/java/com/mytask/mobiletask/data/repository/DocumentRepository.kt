package com.mytask.mobiletask.data.repository

import com.mindorks.framework.mvvm.utils.Constants
import com.mytask.mobiletask.data.model.GeneralResponse
import com.mytask.mobiletask.data.listenners.OnResponseListener
import com.mytask.mobiletask.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentRepository constructor(private val retrofitService: RetrofitService) {

    fun getDocumentsQuery(
       searchText: String?,
       page:Int,
       searchType:String,
        onResponseListener: OnResponseListener
    ) {
        val call: Call<GeneralResponse> = when (searchType) {
            Constants.TYPE_SEARCH_TITLE -> retrofitService.getDocumentsByTitle(searchText,page)
            Constants.TYPE_SEARCH_Author -> retrofitService.getDocumentsByQuery(searchText,page)
            else -> {
                retrofitService.getDocumentsByQuery(searchText,page)
            }
        }
        call.clone().enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.body() != null) {
                    onResponseListener.onSucess(response.body()!!.docs, response.body()!!.num_found)
                } else if (response.code() == SERVER_ERROR) {
                    onResponseListener.onSerVerError("something went wrong ,please try again")
                }else{
                    onResponseListener.onFailure("failure error")
                }
            }

            override fun onFailure(
                call: Call<GeneralResponse>,
                t: Throwable
            ) {
                onResponseListener.onFailure("failure error  ")
            }
        })
    }

    companion object {
        const val SERVER_ERROR = 500

    }

}