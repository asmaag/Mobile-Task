package com.mytask.mobiletask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytask.mobiletask.data.model.Document
import com.mytask.mobiletask.data.listenners.OnResponseListener
import com.mytask.mobiletask.data.repository.DocumentRepository
import kotlinx.coroutines.*


class DocumentViewModel constructor(private val repository: DocumentRepository) : ViewModel() {
    val docsList = MutableLiveData<List<Document>>()
    var lastResult = mutableListOf<Document>()
    var noMoreData = true
    val errorMessage = MutableLiveData<String>()
    private var searchJob: Job? = null

    fun getDocumentsBySearchKey(searchText: String,page:Int,searchType:String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            repository.getDocumentsQuery(
                searchText,
                page, searchType,
                object : OnResponseListener {
                    override fun onSucess(data: List<Document>?, totalDataCount: Int) {
                        if(data!= null) {
                            lastResult = if (page > 1) {
                                (lastResult + data).toMutableList()

                            } else {
                                data as MutableList<Document>

                            }
                            noMoreData = totalDataCount == lastResult.size

                        }
                        docsList.postValue( lastResult)
                    }

                    override fun onFailure(msg:String?) {
                        errorMessage.postValue(msg)
                    }

                    override fun onSerVerError(msg: String?) {
                        errorMessage.postValue(msg)
                    }
                })
        }
    }

    fun loadMorData():Boolean{
            return !noMoreData
    }


}