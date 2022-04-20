package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemsModel(

    @SerializedName("name")
    val nameRepo: String,

    @SerializedName("owner")
    val owner: UserModel,

    @SerializedName("description")
    val description: String,

    @SerializedName("stargazers_count")
    val stars: Int,

    @SerializedName("forks_count")
    val forks: Int

) : Serializable