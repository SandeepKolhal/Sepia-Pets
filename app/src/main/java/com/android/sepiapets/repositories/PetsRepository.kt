package com.android.sepiapets.repositories

import com.android.sepiapets.models.petsData.PetList
import com.android.sepiapets.models.result.ResultData

interface PetsRepository {
    suspend fun getPetsList(): ResultData<PetList>
}