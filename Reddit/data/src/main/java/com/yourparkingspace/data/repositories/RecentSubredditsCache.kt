package com.yourparkingspace.data.repositories

import com.yourparkingspace.database.dao.VisitedSubredditDAO
import com.yourparkingspace.database.entity.VisitedSubredditEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RecentSubredditsCache @Inject constructor(
    private val visitedSubredditsDao: VisitedSubredditDAO
) {
    fun observeRecentlyVisited(): Flow<List<String>> =
        visitedSubredditsDao.observeAll().map { subreddits ->
            subreddits.map { it.name }
        }

    suspend fun addToVisited(subreddit: String) {
        val values = visitedSubredditsDao.getAll()
        val thisSubreddit = values.find { it.name == subreddit }
        val now = System.currentTimeMillis()
        if (thisSubreddit != null) {
            thisSubreddit.dateVisited = now
            visitedSubredditsDao.update(thisSubreddit)
        } else {
            visitedSubredditsDao.insert(VisitedSubredditEntity(subreddit, now))
        }

        if (visitedSubredditsDao.count() > RECENT_SUBREDDITS_LIMIT) {
            val visited = visitedSubredditsDao.getAll()
            val oldest = visited.minByOrNull { it.dateVisited }
            oldest?.let {
                visitedSubredditsDao.delete(it)
            }
        }
    }

    suspend fun deleteFromVisited(subreddit: String) {
        visitedSubredditsDao.deleteByName(subreddit)
    }

    companion object {
        const val RECENT_SUBREDDITS_LIMIT = 15
    }
}