package com.android.sepiapets.dataSource

import com.android.sepiapets.models.config.Config
import com.android.sepiapets.models.result.ErrorData
import com.android.sepiapets.models.result.ResultData
import com.android.sepiapets.utils.Constants
import com.android.sepiapets.utils.FileOperations
import com.google.gson.Gson

class ConfigLocalDataSourceImpl(private val fileOperations: FileOperations) : ConfigDataSource {
    override suspend fun fetchConfigData(): ResultData<Config> {
        return try {
            val jsonString = fileOperations.getDataFromFile(Constants.CONFIG_DATA_PATH)
            ResultData.Success(Gson().fromJson(jsonString, Config::class.java))
        } catch (e: Exception) {
            ResultData.Failure(
                ErrorData(
                    e.message ?: "Error in fetching config Data", e
                )
            )
        }
    }
}