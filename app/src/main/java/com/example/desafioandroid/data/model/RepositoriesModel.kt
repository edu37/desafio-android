package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RepositoriesModel(

    @SerializedName("items")
    val items: ItemsModel

): Serializable
