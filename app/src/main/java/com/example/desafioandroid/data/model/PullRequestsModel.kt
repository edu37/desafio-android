package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
