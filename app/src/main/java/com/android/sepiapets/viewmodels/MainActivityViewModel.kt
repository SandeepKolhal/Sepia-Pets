package com.android.sepiapets.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sepiapets.models.config.Config
import com.android.sepiapets.models.config.WorkingHours
import com.android.sepiapets.models.petsData.PetList
import com.android.sepiapets.models.result.ErrorData
import com.android.sepiapets.models.result.ResultData
import com.android.sepiapets.repositories.ConfigRepository
import com.android.sepiapets.repositories.PetsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val petsRepository: PetsRepository,
    private val configRepository: ConfigRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _petList = MutableLiveData<PetList?>()
    val petList: LiveData<PetList?>
        get() = _petList

    private val _petErrorData = MutableLiveData<ErrorData>()
    val petErrorData: LiveData<ErrorData>
        get() = _petErrorData

    private val _configData = MutableLiveData<Config?>()
    val configData: LiveData<Config?>
        get() = _configData

    private val _configErrorData = MutableLiveData<ErrorData>()
    val configErrorData: LiveData<ErrorData>
        get() = _configErrorData


    fun getPetsList() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = petsRepository.getPetsList()) {
                is ResultData.Failure -> {
                    _petErrorData.postValue(result.error)
                }
                is ResultData.Success -> {
                    _petList.postValue(result.value)
                }
            }
        }
    }

    fun getConfigData() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = configRepository.getConfigData()) {
                is ResultData.Failure -> {
                    _configErrorData.postValue(result.error)
                }
                is ResultData.Success -> {
                    _configData.postValue(result.value)
                }
            }
        }
    }

    fun checkWorkingHours(config: Config, currentDay: Int, currentHour: Int): Boolean {
        return when (val result = getDataFromConfig(config)) {
            is ResultData.Failure -> false
            is ResultData.Success -> {
                val workingData = result.value
                (currentDay in workingData.startDay..workingData.endTime && currentHour in workingData.startTime..workingData.endTime)
            }
        }
    }

    private fun getDataFromConfig(config: Config): ResultData<WorkingHours> {
        return try {
            val workingHours = config.settings.workHours  //"M-F 9:00 - 18:00"
            val listOfData = workingHours.split(" ")

            //this is to get day data
            val listOfDay = listOfData[0].split("-")
            val dayData = getDayOfWeek(listOfDay)

            val startTime = listOfData[1].split(":")[0].toInt()
            val endTimeResult = listOfData[3].split(":")[0].toInt()
            val endTime = if (endTimeResult > 0) {
                endTimeResult - 1
            } else {
                0
            }

            ResultData.Success(
                WorkingHours(
                    startDay = dayData.first,
                    endDay = dayData.second,
                    startTime = startTime,
                    endTime = endTime
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
            ResultData.Failure(ErrorData(e.message ?: "Error in parsing working data", e))
        }
    }

    private fun getDayOfWeek(listOfDay: List<String>): Pair<Int, Int> {
        var startDay = 0
        var endDay = 0
        repeat(listOfDay.size) {
            if (it == 0) {
                startDay = getDay(listOfDay[it])
            } else {
                endDay = getDay(listOfDay[it])
            }
        }

        return Pair(startDay, endDay)
    }

    private fun getDay(day: String): Int {
        return when (day) {
            "S", "s" -> 1
            "M", "m" -> 2
            "Tu", "tu", "TU" -> 3
            "W", "w" -> 4
            "Th", "th", "TH" -> 5
            "F", "f" -> 6
            "Sa", "SA", "sa" -> 7
            else -> 0
        }
    }

}