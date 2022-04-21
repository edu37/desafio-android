package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RepositoriesModelResponse(

    @SerializedName("items")
    val items: List<RepositoriesModel>

): Serializable
