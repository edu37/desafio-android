package com.example.desafioandroid.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** Para pegar o conteúdo do "owner" no caso do repositório e pegar o conteúdo de "user" no pull request */
data class UserModel(

    @SerializedName("login")
    val name: String,

    @SerializedName("avatar_url")
    val avatar: String,

    @SerializedName("html_url")
    val userUrl: String

) : Serializable
