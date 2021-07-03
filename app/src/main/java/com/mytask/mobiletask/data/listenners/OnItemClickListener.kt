package com.mytask.mobiletask.data.listenners

import com.mytask.mobiletask.data.model.Document

interface OnItemClickListener {
    fun onCickItem(document: Document?)

}