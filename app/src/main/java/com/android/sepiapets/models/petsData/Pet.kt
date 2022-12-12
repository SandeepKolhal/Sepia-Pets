package com.android.sepiapets.models.petsData


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    @SerializedName("content_url")
    val contentUrl: String,
    @SerializedName("date_added")
    val dateAdded: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("title")
    val title: String
) : Parcelable