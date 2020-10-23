package com.masykur.githubuser2.models

import android.content.ContentValues
import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "userDetail")
data class Item(
    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    var id:Int = 0,

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String ="",



    @ColumnInfo(name = "followers")
    var followers: Int = 0,

    @ColumnInfo(name = "following")
    var following: Int = 0,

    @ColumnInfo(name = "login")
    var login: String ="",

    @ColumnInfo(name = "name")
    var name: String ="",

    @ColumnInfo(name = "company")
    var company: String ="",

    @ColumnInfo(name = "location")
    var location: String ="",

    @ColumnInfo(name = "public_repos")
    var public_repos: Int=0,

):Serializable{
    companion object{
        const val TABLE_NAME = "userDetail"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_AVATAR_URL = "avatar_url"
        const val COLUMN_FOLLOWERS = "followers"
        const val COLUMN_FOLLOWING = "following"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_NAME = "name"
        const val COLUMN_COMPANY = "company"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_PUBLIC_REPOS = "public_repos"

        fun fromContentValues(contentValues: ContentValues?): Item {
            val userDetail = Item(0,"",0,0,"","","","",0,)
            contentValues?.apply {

                if (containsKey(COLUMN_ID)) {
                    userDetail.id = getAsInteger(COLUMN_ID)
                }
                if (containsKey(COLUMN_AVATAR_URL)) {
                    userDetail.avatar_url = getAsString(COLUMN_AVATAR_URL)
                }
                if (containsKey(COLUMN_FOLLOWERS)) {
                    userDetail.followers = getAsInteger(COLUMN_FOLLOWERS)
                }
                if (containsKey(COLUMN_FOLLOWING)) {
                    userDetail.following = getAsInteger(COLUMN_FOLLOWING)
                }
                if (containsKey(COLUMN_LOGIN)) {
                    userDetail.login = getAsString(COLUMN_LOGIN)
                }
                if (containsKey(COLUMN_NAME)) {
                    userDetail.name = getAsString(COLUMN_NAME)
                }
                if (containsKey(COLUMN_COMPANY)) {
                    userDetail.company = getAsString(COLUMN_COMPANY)
                }
                if (containsKey(COLUMN_LOCATION)) {
                    userDetail.location = getAsString(COLUMN_LOCATION)
                }
                if (containsKey(COLUMN_PUBLIC_REPOS)) {
                    userDetail.public_repos = getAsInteger(COLUMN_PUBLIC_REPOS)
                }
            }
            return userDetail
        }



    }

}

