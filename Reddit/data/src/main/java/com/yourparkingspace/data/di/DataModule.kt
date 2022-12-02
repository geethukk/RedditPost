package com.yourparkingspace.data.di

import android.content.Context
import android.content.SharedPreferences
import com.yourparkingspace.data.repositories.Authenticator
import com.yourparkingspace.data.repositories.CommentRepository
import com.yourparkingspace.domain.repository.IAuthenticator
import com.yourparkingspace.domain.repository.IPostRepo
import com.yourparkingspace.data.repositories.PostRepo
import com.yourparkingspace.data.repositories.SubredditRepository
import com.yourparkingspace.domain.repository.ICommentRepository
import com.yourparkingspace.domain.repository.ISubredditRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataModule {

    @Provides
    @Singleton
    fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("redditclientprefs", Context.MODE_PRIVATE)
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun userRepo(authenticator: Authenticator): IAuthenticator

    @Binds
    abstract fun postRepo(repo: PostRepo): IPostRepo

    @Binds
    abstract fun subredditRepo(repo: SubredditRepository) : ISubredditRepository

    @Binds
    abstract fun commentRepo(repo: CommentRepository) : ICommentRepository

}
