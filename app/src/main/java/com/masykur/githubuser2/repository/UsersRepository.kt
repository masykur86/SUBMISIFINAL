package com.masykur.githubuser2.repository

import android.content.ContentValues
import com.masykur.githubuser2.api.RetrofitInstance
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.models.Item

class UsersRepository(val db:UserDetail) {

    suspend fun getAllUsers(apiKey :String) = RetrofitInstance.api.getUser(apiKey)

    suspend fun searchUser(userName:String,apiKey :String) =
        RetrofitInstance.api.searchUsers(userName,apiKey)

     suspend fun getUserDetails(username:String,apiKey :String) =
        RetrofitInstance.api.findUserDetailByUsername(username,apiKey)

    suspend fun getAllUsersFollowing(userName:String,apiKey :String) =
        RetrofitInstance.api.findUserFollowing(userName,apiKey)

    suspend fun getAllUsersFollowers(userName:String,apiKey :String)=
        RetrofitInstance.api.findUserFollowers(userName,apiKey)

    suspend fun upsert(userDetail: Item) = db.getUserDetail().upsert(userDetail)


   suspend  fun getSavedUser() = db.getUserDetail().getAllUsers()

    suspend fun deleteArticle(userDetail: Item) = db.getUserDetail().deleteUsers(userDetail)


}