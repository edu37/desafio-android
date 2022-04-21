package com.example.desafioandroid.repository

import android.content.SharedPreferences
import com.example.desafioandroid.data.model.PullRequestsModel
import com.example.desafioandroid.data.model.RepositoriesModelResponse
import com.example.desafioandroid.data.remote.ServiceApi
import com.example.desafioandroid.ui.state.State
import com.example.desafioandroid.util.Constants.REPO_NAME
import com.example.desafioandroid.util.Constants.USER_NAME
import com.google.gson.Gson
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val api: ServiceApi,
    private val shared: SharedPreferences
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

    fun saveShared(userName: String, repoName: String) {
        shared.edit().putString(USER_NAME, userName).apply()
        shared.edit().putString(REPO_NAME, repoName).apply()
    }

    suspend fun pullRequests(): State<List<PullRequestsModel>> {
        val userName = shared.getString(USER_NAME, "") ?: ""
        val repoName = shared.getString(REPO_NAME, "") ?: ""
        val response = api.pullRequests(userName, repoName)

        return if (response.isSuccessful) {
            State.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()?.charStream(), String::class.java)
            State.Error(message)
        }
    }

}