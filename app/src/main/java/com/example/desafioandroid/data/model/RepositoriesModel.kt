package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** dataClass que representa o conteúdo de cada ítem(repositório) que acessamos através do objeto recebido */
data class RepositoriesModel(

    @SerializedName("name")
    val repoName: String,

    @SerializedName("owner")
    val owner: UserModel,

    @SerializedName("description")
    val description: String,

    @SerializedName("stargazers_count")
    val stars: Int,

    @SerializedName("forks_count")
    val forks: Int

) : Serializable
