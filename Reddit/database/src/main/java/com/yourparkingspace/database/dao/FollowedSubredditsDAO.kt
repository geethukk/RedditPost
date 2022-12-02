package com.yourparkingspace.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yourparkingspace.database.entity.FollowedSubredditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowedSubredditsDAO : BaseDAO<FollowedSubredditEntity> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insert(t: FollowedSubredditEntity)

    @Query("SELECT * FROM FollowedSubredditEntity")
    fun observeAll() : Flow<List<FollowedSubredditEntity>>

    @Query("SELECT * FROM FollowedSubredditEntity WHERE name=:name")
    suspend fun getByName(name: String) : FollowedSubredditEntity?

    @Query("DELETE FROM FollowedSubredditEntity WHERE name=:name")
    suspend fun deleteByName(name: String)
}