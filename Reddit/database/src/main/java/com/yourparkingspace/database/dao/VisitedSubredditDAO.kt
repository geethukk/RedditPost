package com.yourparkingspace.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.yourparkingspace.database.entity.VisitedSubredditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitedSubredditDAO : BaseDAO<VisitedSubredditEntity> {

    @Query("SELECT * FROM VisitedSubredditEntity ORDER BY dateVisited DESC")
    fun observeAll() : Flow<List<VisitedSubredditEntity>>

    @Query("SELECT * FROM VisitedSubredditEntity ORDER BY dateVisited DESC")
    suspend fun getAll() : List<VisitedSubredditEntity>

    @Query("SELECT * FROM VisitedSubredditEntity WHERE name=:name")
    suspend fun getByName(name: String) : VisitedSubredditEntity

    @Query("DELETE FROM VisitedSubredditEntity WHERE name=:name")
    suspend fun deleteByName(name: String)

    @Query("SELECT COUNT(*) FROM VisitedSubredditEntity")
    suspend fun count(): Int
}