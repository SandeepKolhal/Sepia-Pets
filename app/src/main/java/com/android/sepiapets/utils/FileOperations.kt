package com.android.sepiapets.utils

import android.content.Context

class FileOperations(private val context: Context) {
    fun getDataFromFile(filePath: String): String {
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }
}