package com.yourparkingspace.data.repositories

import com.yourparkingspace.data.mappers.mapToDomain
import com.yourparkingspace.data.mappers.mapToEntity
import com.yourparkingspace.database.dao.FollowedSubredditsDAO
import com.yourparkingspace.database.dao.SubredditDAO
import com.yourparkingspace.database.entity.FollowedSubredditEntity
import com.yourparkingspace.database.entity.SubredditEntity
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.domain.model.SubscriptionState
import com.yourparkingspace.network.endpoints.SubredditsEndpoint
import com.yourparkingspace.data.mappers.toDomainSubreddit
import com.yourparkingspace.domain.repository.ISubredditRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SubredditRepository @Inject constructor(
    private val subredditsEndpoint: SubredditsEndpoint,
    private val subredditDAO: SubredditDAO,
    private val followedSubredditsDAO: FollowedSubredditsDAO,
    private val recentlyVisitedCache: RecentSubredditsCache
) : ISubredditRepository {

    override fun observeRecentlyVisited(): Flow<List<Subreddit>> =
        recentlyVisitedCache
            .observeRecentlyVisited()
            .map { names ->
                names.mapNotNull {
                    getSubreddit(it, reload = false)
                }
            }

    override suspend fun visited(subreddit: String) {
        recentlyVisitedCache.addToVisited(subreddit)
    }

    override suspend fun deleteFromVisited(subreddit: String) {
       recentlyVisitedCache.deleteFromVisited(subreddit)
    }

    override suspend fun changeSubscriptionState(subreddit: Subreddit): AppResult<Subreddit> {
        val newState = if (subreddit.isSubscribed)
            SubscriptionState.NotSubscribed
        else
            SubscriptionState.Subscribed(isFavorite = false)

        if (newState == SubscriptionState.NotSubscribed) {
            followedSubredditsDAO.deleteByName(subreddit.name)
        } else {
            followedSubredditsDAO.insert(FollowedSubredditEntity(subreddit.name, false))
        }

        return AppResult.success(subreddit.copy(subscriptionState = newState))
    }

    override suspend fun setFavorite(subreddit: Subreddit, favorite: Boolean) : AppResult<Unit> {
        val name = subreddit.name
        val cached = followedSubredditsDAO.getByName(name)
        if (cached == null) {
            followedSubredditsDAO.insert(FollowedSubredditEntity(name, favorite))
        } else {
            followedSubredditsDAO.update(cached.apply { isFavorite = favorite })
        }

        return AppResult.Success(Unit)
    }

    private suspend fun Subreddit.setupSubscriptionState() : Subreddit {
        val followed = followedSubredditsDAO.getByName(name)
        subscriptionState = when (followed) {
            null -> SubscriptionState.NotSubscribed
            else -> SubscriptionState.Subscribed(followed.isFavorite)
        }
        return this
    }

    override suspend fun getSubreddit(name: String, reload: Boolean): Subreddit? {

        if (!reload) {
            val cached = subredditDAO.getByName(name)
            if (cached != null) {
                return cached.mapToDomain().apply {
                    setupSubscriptionState()
                }
            }
        }

        val result = subredditsEndpoint.getSubreddit(name)

        if (result is AppResult.Success) {
            val sub = result.data
            val domainSub = sub.toDomainSubreddit() ?: return null
            return domainSub.apply {
                setupSubscriptionState()
            }
        }

        return null
    }

    override suspend fun getSubscribedSubreddits(): Flow<List<Subreddit>> {
        return followedSubredditsDAO.observeAll()
            .map { list -> list.mapNotNull { getSubreddit(it.name, reload = false) } }
    }

    private fun didExpire(dateModified: Long, expirationTime: Long) : Boolean {
        val now = System.currentTimeMillis()
        val difference = now - dateModified
        return difference > expirationTime
    }

    override suspend fun getSubredditIcon(name: String): String? {
        val cached = subredditDAO.getByName(name)
        return if (cached != null) {
            if (didExpire(cached.dateModified, EXPIRE_MS_SUBREDDIT_ICON)) {
                fetchAndCacheSubreddit(name)?.iconUrl
            } else {
                cached.iconUrl
            }
        } else {
            fetchAndCacheSubreddit(name)?.iconUrl
        }
    }

    private suspend fun fetchAndCacheSubreddit(name: String): SubredditEntity? {
        val freshData = subredditsEndpoint.getSubreddit(name)
        val cached = subredditDAO.getByName(name)
        return if (freshData is AppResult.Success) {
            val subreddit = freshData.data.toDomainSubreddit() ?: return cached
            val entity = subreddit.mapToEntity()
            subredditDAO.insert(entity)
            entity
        } else {
            cached
        }
    }

    override fun getSubredditFlow(name: String) : Flow<Subreddit?> = flow {
        val cached = subredditDAO.getByName(name)
        if (cached != null) {
            emit(cached.mapToDomain().setupSubscriptionState())
        }

        val fresh = fetchAndCacheSubreddit(name)
        if (fresh != null) {
            emit(fresh.mapToDomain().setupSubscriptionState())
        }
    }

    private companion object {

        const val EXPIRE_MS_SUBREDDIT_ICON : Long = 24 * 60 * 60 * 1000
        const val EXPIRE_MS_SUBREDDIT : Long = 24 * 60 * 60 * 1000
    }
}