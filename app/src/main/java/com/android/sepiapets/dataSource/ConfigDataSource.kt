package com.android.sepiapets.dataSource

import com.android.sepiapets.models.config.Config
import com.android.sepiapets.models.result.ResultData

interface ConfigDataSource {
    suspend fun fetchConfigData(): ResultData<Config>
}