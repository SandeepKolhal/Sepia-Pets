package com.android.sepiapets.repositories

import com.android.sepiapets.models.config.Config
import com.android.sepiapets.models.result.ResultData

interface ConfigRepository {
    suspend fun getConfigData(): ResultData<Config>
}