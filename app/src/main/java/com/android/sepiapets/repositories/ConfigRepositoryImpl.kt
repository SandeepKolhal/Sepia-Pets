package com.android.sepiapets.repositories

import com.android.sepiapets.dataSource.ConfigDataSource
import com.android.sepiapets.models.config.Config
import com.android.sepiapets.models.result.ResultData

class ConfigRepositoryImpl(private val configDataSource: ConfigDataSource) : ConfigRepository {
    override suspend fun getConfigData(): ResultData<Config> {
        return configDataSource.fetchConfigData()
    }
}