package com.android.sepiapets.dataSource

import android.util.Log
import com.android.sepiapets.models.petsData.PetList
import com.android.sepiapets.models.result.ErrorData
import com.android.sepiapets.models.result.ResultData
import com.android.sepiapets.utils.Constants
import com.android.sepiapets.utils.FileOperations
import com.google.gson.Gson

class PetLocalDataSourceImpl(private val fileOperations: FileOperations) : PetDataSource {

    override suspend fun fetchPetListData(): ResultData<PetList> {
        return try {
            val jsonString = fileOperations.getDataFromFile(Constants.PET_DATA_PATH)
            Log.i(TAG, jsonString)
            ResultData.Success(Gson().fromJson(jsonString, PetList::class.java))
        } catch (e: Exception) {
            ResultData.Failure(
                ErrorData(
                    e.message ?: "Error in fetching pet list", e
                )
            )
        }
    }

    companion object {
        const val TAG = "PetLocalDataSourceImpl"
    }
}