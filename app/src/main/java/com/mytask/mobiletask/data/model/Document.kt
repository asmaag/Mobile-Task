package com.mytask.mobiletask.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Document  (

    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("author_name")
    val authors : List<String>?,
    @SerializedName("isbn")
       val isbn : List<String>?
) : Parcelable
