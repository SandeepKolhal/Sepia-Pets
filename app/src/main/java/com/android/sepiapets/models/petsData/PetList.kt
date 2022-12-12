package com.android.sepiapets.models.petsData


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PetList(
    @SerializedName("pets")
    val pets: List<Pet>
) : Parcelable