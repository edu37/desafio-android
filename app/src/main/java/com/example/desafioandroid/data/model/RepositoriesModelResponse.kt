package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** dataClass que representa o objeto recebido pela chamada da Api search/repositories */
data class RepositoriesModelResponse(

    @SerializedName("items")
    val items: List<RepositoriesModel>

) : Serializable
