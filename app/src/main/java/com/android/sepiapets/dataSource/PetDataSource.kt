package com.android.sepiapets.dataSource

import com.android.sepiapets.models.petsData.PetList
import com.android.sepiapets.models.result.ResultData

interface PetDataSource {
    suspend fun fetchPetListData(): ResultData<PetList>
}