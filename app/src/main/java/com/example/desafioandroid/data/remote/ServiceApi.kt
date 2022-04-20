package com.example.desafioandroid.data.remote

import com.example.desafioandroid.data.model.PullRequestsModel
import com.example.desafioandroid.data.model.RepositoriesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("/search/repositories")
    suspend fun repositories(
        @Query("q") q: String = "language:Java",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int = 1
    ): Response<List<RepositoriesModel>>

    @GET("/repos/{owner}/{repo}/pulls")
    suspend fun pullRequests(
        @Path(value = "owner", encoded = true) owner: String,
        @Path(value = "repo", encoded = true) repo: String
    ): Response<List<PullRequestsModel>>
}