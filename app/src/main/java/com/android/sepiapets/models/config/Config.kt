package com.android.sepiapets.models.config


import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("settings")
    val settings: Settings
)