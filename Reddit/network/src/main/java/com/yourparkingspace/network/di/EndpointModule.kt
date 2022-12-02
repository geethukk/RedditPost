package com.yourparkingspace.network.di

import com.yourparkingspace.network.endpoints.PostsEndpoint
import com.yourparkingspace.network.endpoints.SubredditsEndpoint
import com.yourparkingspace.network.endpoints.PostsEndpointImpl
import com.yourparkingspace.network.endpoints.SubredditsEndpointImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class EndpointModule {

    @Binds
    abstract fun bindPostsEndpoint(impl: PostsEndpointImpl): PostsEndpoint

    @Binds
    abstract fun bindSubredditsEndpoint(impl: SubredditsEndpointImpl): SubredditsEndpoint
}