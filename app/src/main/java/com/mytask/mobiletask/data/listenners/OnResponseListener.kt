package com.mytask.mobiletask.data.listenners

import com.mytask.mobiletask.data.model.Document

interface OnResponseListener {
    fun onSucess(data: List<Document>?, totalDataCount: Int)

    fun onFailure(msg: String?)

    fun onSerVerError(msg: String?)
}