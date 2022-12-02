package com.yourparkingspace.database.entity

import androidx.room.*

@Entity
data class SubredditEntity(
    @PrimaryKey
    val name: String,
    var description: String,
    var subscriptions: Int,
    var activeUsers: Int,
    var backgroundImage: String?,
    var keyColor: String?,
    var iconUrl: String?,
    var dateModified: Long,
)