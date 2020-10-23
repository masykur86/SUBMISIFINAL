package com.masykur.githubuser2.db

import android.content.Context
import android.net.Uri
import androidx.room.*
import com.masykur.githubuser2.models.Item



@Database(
    entities = [Item::class],
    version = 2
)

abstract class UserDetail : RoomDatabase() {

    abstract fun getUserDetail(): UserDao

    companion object {
        @Volatile
        private var instance: UserDetail? = null
        private val LOCK = Any()
       operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDetail::class.java,
                "github_db.db"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()


    }

    }
