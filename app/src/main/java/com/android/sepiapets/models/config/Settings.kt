package com.android.sepiapets.models.config


import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("workHours")
    val workHours: String
)