package com.example.desafioandroid.repository

import android.content.SharedPreferences
import com.example.desafioandroid.data.model.*
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

    /** Salva no SharedPreferences o login do usuário e o nome do repositório em questão, que está sendo clicado para poder utilizar posteriormente para pegar os pulls requests referentes a este repositório deste usuário.*/
    fun saveShared(userName: String, repoName: String) {
        shared.edit().putString(USER_NAME, userName).apply()
        shared.edit().putString(REPO_NAME, repoName).apply()
    }

    /** Recebe a resposta da Api na busca pelo objeto que contém a lista de repositórios. */
    suspend fun repositories(): State<List<RepositoriesModel>> {
        val response = api.repositories()
        return if (response.isSuccessful) {
            State.Sucess(response.body()?.items)
        } else {
            val message = Gson().fromJson(response.errorBody()?.charStream(), String::class.java)
            State.Error(message)
        }
    }

    /** Recebe a resposta da Api na busca pela lista de pull requests. */
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