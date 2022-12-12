package com.android.sepiapets.models.result

data class ErrorData(
    val message: String, val throwable: Throwable? = null
)
