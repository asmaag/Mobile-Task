package com.mytask.mobiletask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mytask.mobiletask.data.repository.DocumentRepository

class ViewModelFactory constructor(private val repository: DocumentRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DocumentViewModel::class.java)) {
            DocumentViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}