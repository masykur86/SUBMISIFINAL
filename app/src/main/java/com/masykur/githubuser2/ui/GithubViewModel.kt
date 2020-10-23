package com.masykur.githubuser2.ui

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.masykur.githubuser2.models.GithubData
import com.masykur.githubuser2.GithubApplication
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.models.Item
import com.masykur.githubuser2.repository.UsersRepository
import com.masykur.githubuser2.utils.Constant
import com.masykur.githubuser2.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class GithubViewModel(val gitRepo: UsersRepository, app: Application, val context: Context) :
    AndroidViewModel(app) {
    //    respon akan diambil dari resource, dipili yang sesuai
    val gitUsers = MutableLiveData<List<Item>>()
    val gitUsersFollowing = MutableLiveData<List<Item>>()
    val gitUsersFollowers = MutableLiveData<List<Item>>()
    val gitUsersDetail = MutableLiveData<Item>()
    val searchUser: MutableLiveData<Resource<GithubData>> = MutableLiveData()


    init {
        getAllUsers()
    }

     fun getAllUsers() = viewModelScope.launch {
        try {
            if (getInternetConnection()) {
                val response = gitRepo.getAllUsers(Constant.API_KEY)
                Log.d("tes", response.toString())
                gitUsers.postValue(response)

            } else {
                Toast.makeText(context, "tidak ada koneksi", Toast.LENGTH_SHORT).show()

            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(context, "kesalahan jaringan", Toast.LENGTH_SHORT)
                    .show()
                else -> Toast.makeText(context, "kesalahan konversi", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun getDetailUsers(username: String) = viewModelScope.launch {
        try {
            if (getInternetConnection()) {
                val response = gitRepo.getUserDetails(username,Constant.API_KEY)
                gitUsersDetail.value = response

            } else {
                Toast.makeText(context, "tidak ada koneksi", Toast.LENGTH_SHORT).show()

            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(context, "kesalahan jaringan", Toast.LENGTH_SHORT)
                    .show()
//                else -> Toast.makeText(context, "kesalahan konversi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getUsersFollowers(username: String) = viewModelScope.launch {
        try {
            if (getInternetConnection()) {
                val response = gitRepo.getAllUsersFollowers(username,Constant.API_KEY)
                Log.d("tes", response.toString())
                gitUsersFollowers.postValue(response)

            } else {
                Toast.makeText(context, "tidak ada koneksi", Toast.LENGTH_SHORT).show()

            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(context, "kesalahan jaringan", Toast.LENGTH_SHORT)
                    .show()
                else -> Toast.makeText(context, "kesalahan konversi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getUsersFollowing(username: String) = viewModelScope.launch {
        try {
            if (getInternetConnection()) {
                val response = gitRepo.getAllUsersFollowing(username,Constant.API_KEY)
                Log.d("tes", response.toString())
                gitUsersFollowing.postValue(response)


            } else {
                Toast.makeText(context, "tidak ada koneksi", Toast.LENGTH_SHORT).show()

            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(context, "kesalahan jaringan", Toast.LENGTH_SHORT)
                    .show()
                else -> Toast.makeText(context, "kesalahan konversi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun searchUser(query: String) = viewModelScope.launch {
        getUserSearch(query)

    }
     suspend fun getUserSearch (query: String){
        searchUser.postValue(Resource.Loading())
        try {
            if (getInternetConnection()){
                val response = gitRepo.searchUser(query,Constant.API_KEY)
//        mengambil respon baru
                searchUser.postValue(hadleSearchNewsResponse(response))

            }else{
                searchUser.postValue(Resource.Error("tidak ada koneksi"))

            }

        }catch (t : Throwable){
            when (t){
                is IOException -> searchUser.postValue(Resource.Error("kesalahan jaringan"))
                else -> searchUser.postValue(Resource.Error("kesalahan conversi"))
            }
        }
    }

    private fun hadleSearchNewsResponse(response: Response<GithubData>): Resource<GithubData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                return Resource.Succces(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveUser(userDetail: Item) = viewModelScope.launch {
        gitRepo.upsert(userDetail)
    }

     fun getSavedUser() =  viewModelScope.launch {
         gitRepo.getSavedUser()
     }

    fun deleteUser(userDetail: Item) = viewModelScope.launch {
        gitRepo.deleteArticle(userDetail)
    }




    private fun getInternetConnection(): Boolean {
        val connectivityManager = getApplication<GithubApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}