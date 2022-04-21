package com.example.desafioandroid.repository

import com.example.desafioandroid.data.model.RepositoriesModelResponse
import com.example.desafioandroid.data.remote.ServiceApi
import com.example.desafioandroid.ui.state.State
import com.google.gson.Gson
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val api: ServiceApi
) {
    suspend fun repositories(): State<RepositoriesModelResponse> {
        val response = api.repositories()
        return if (response.isSuccessful) {
            State.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()?.charStream(), String::class.java)
            State.Error(message)
        }
    }


}