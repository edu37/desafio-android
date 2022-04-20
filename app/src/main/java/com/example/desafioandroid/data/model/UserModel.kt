package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(

    @SerializedName("login")
    val name: String,

    @SerializedName("avatar_url")
    val avatar: String

): Serializable
