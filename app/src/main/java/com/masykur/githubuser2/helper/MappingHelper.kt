package com.masykur.githubuser2.helper

import android.database.Cursor
import android.provider.ContactsContract
import com.masykur.githubuser2.models.Item
import java.util.ArrayList

object MappingHelper {

    
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<Item> {
        val userList = ArrayList<Item>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Item.COLUMN_ID))
                val avatar_url = getString(getColumnIndexOrThrow(Item.COLUMN_AVATAR_URL))
                val followers = getInt(getColumnIndexOrThrow(Item.COLUMN_FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(Item.COLUMN_FOLLOWING))
                val login = getString(getColumnIndexOrThrow(Item.COLUMN_LOGIN))
                val name = getString(getColumnIndexOrThrow(Item.COLUMN_NAME))
                val company = getString(getColumnIndexOrThrow(Item.COLUMN_COMPANY))
                val location = getString(getColumnIndexOrThrow(Item.COLUMN_LOCATION))
                val public_repos = getInt(getColumnIndexOrThrow(Item.COLUMN_PUBLIC_REPOS))
                userList.add(Item(id,avatar_url,followers,following,login,name,company,location,public_repos))
            }
        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?): Item {
        var userList = Item(0,"",0,0,"","","","",0,)
        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(Item.COLUMN_ID))
            val avatar_url = getString(getColumnIndexOrThrow(Item.COLUMN_AVATAR_URL))
            val followers = getInt(getColumnIndexOrThrow(Item.COLUMN_FOLLOWERS))
            val following = getInt(getColumnIndexOrThrow(Item.COLUMN_FOLLOWING))
            val login = getString(getColumnIndexOrThrow(Item.COLUMN_LOGIN))
            val name = getString(getColumnIndexOrThrow(Item.COLUMN_NAME))
            val company = getString(getColumnIndexOrThrow(Item.COLUMN_COMPANY))
            val location = getString(getColumnIndexOrThrow(Item.COLUMN_LOCATION))
            val public_repos = getInt(getColumnIndexOrThrow(Item.COLUMN_PUBLIC_REPOS))
            userList =Item(id,avatar_url,followers,following,login,name,company,location,public_repos)
        }
        return userList
    }



}