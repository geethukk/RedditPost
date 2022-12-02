package com.yourparkingspace.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yourparkingspace.database.dao.FollowedSubredditsDAO
import com.yourparkingspace.database.dao.SubredditDAO
import com.yourparkingspace.database.dao.VisitedSubredditDAO
import com.yourparkingspace.database.entity.FollowedSubredditEntity
import com.yourparkingspace.database.entity.SubredditEntity
import com.yourparkingspace.database.entity.VisitedSubredditEntity

@Database(
    entities = [
        SubredditEntity::class,
        FollowedSubredditEntity::class,
        VisitedSubredditEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subredditDao(): SubredditDAO

    abstract fun followedSubredditDao(): FollowedSubredditsDAO

    abstract fun visitedSubredditsDao(): VisitedSubredditDAO

    companion object {

        private const val DATABASE_NAME = "reddit_app_db"

        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getInMemoryDatabase(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

