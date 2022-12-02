package com.yourparkingspace.database

import android.content.Context
import com.yourparkingspace.database.dao.SubredditDAO
import com.yourparkingspace.database.dao.FollowedSubredditsDAO
import com.yourparkingspace.database.dao.VisitedSubredditDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Singleton
    @Provides
    fun database(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun subredditDao(database: AppDatabase): SubredditDAO {
        return database.subredditDao()
    }

    @Provides
    fun followedSubredditDao(database: AppDatabase): FollowedSubredditsDAO {
        return database.followedSubredditDao()
    }

    @Provides
    fun visitedSubredditDao(database: AppDatabase): VisitedSubredditDAO {
        return database.visitedSubredditsDao()
    }
}