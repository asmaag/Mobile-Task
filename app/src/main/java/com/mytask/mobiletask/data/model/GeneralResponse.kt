package com.mytask.mobiletask.data.model

data class GeneralResponse(val start : String,
                           val docs : List<Document>,val num_found:Int)