package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** dataClass que recebe a resposta vinda da Api e já é o acesso direto ao conteúdo dos ítens(pull requests) */
data class PullRequestsModel(

    @SerializedName("html_url")
    val urlPull: String,

    @SerializedName("user")
    val user: UserModel,

    @SerializedName("title")
    val title: String,

    @SerializedName("created_at")
    val date: String,

    @SerializedName("body")
    val body: String?

) : Serializable
