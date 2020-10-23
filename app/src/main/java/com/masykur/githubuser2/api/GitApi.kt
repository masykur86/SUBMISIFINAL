package com.masykur.githubuser2.api

import com.masykur.githubuser2.models.GithubData
import com.masykur.githubuser2.models.Item
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApi {

    @GET("/users/{username}")
    suspend fun findUserDetailByUsername(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ) :Item

    @GET("/users")
     suspend fun getUser(
        @Header("Authorization") token: String

    ) :List<Item>

    @GET("/users/{username}/followers")
     suspend fun findUserFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ) :List<Item>

    @GET("/users/{username}/following")
    suspend fun findUserFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ) :List<Item>

    @GET("/search/users")
     suspend fun searchUsers(
        @Query("q") username: String,
        @Header("Authorization") token: String
    ) :Response<GithubData>
}