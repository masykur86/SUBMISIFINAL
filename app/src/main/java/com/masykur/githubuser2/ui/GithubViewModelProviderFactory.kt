package com.masykur.githubuser2.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.masykur.githubuser2.repository.UsersRepository

//pass application
class GithubViewModelProviderFactory(val app:Application, private val gitUser :UsersRepository,val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  GithubViewModel(gitUser,app,context) as T


    }
}