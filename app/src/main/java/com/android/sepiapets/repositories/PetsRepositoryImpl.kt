package com.android.sepiapets.repositories

import com.android.sepiapets.dataSource.PetDataSource
import com.android.sepiapets.models.petsData.PetList
import com.android.sepiapets.models.result.ResultData

class PetsRepositoryImpl(private val petDataSource: PetDataSource) : PetsRepository {
    override suspend fun getPetsList(): ResultData<PetList> {
        return petDataSource.fetchPetListData()
    }
}