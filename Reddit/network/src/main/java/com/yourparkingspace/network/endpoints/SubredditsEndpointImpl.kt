package com.yourparkingspace.network.endpoints

import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.network.RedditApiService
import com.yourparkingspace.network.mapToDataResult
import com.yourparkingspace.network.models.SubredditAboutDTO
import com.yourparkingspace.network.safeCall
import javax.inject.Inject
import javax.inject.Singleton

interface SubredditsEndpoint {
    suspend fun getSubreddit(name: String): AppResult<SubredditAboutDTO>
}

@Singleton
internal class SubredditsEndpointImpl @Inject constructor(
    private val service: RedditApiService
) : SubredditsEndpoint {
    override suspend fun getSubreddit(name: String): AppResult<SubredditAboutDTO> {
        return safeCall { service.getSubredditInfo(name) }.mapToDataResult()
    }
}
