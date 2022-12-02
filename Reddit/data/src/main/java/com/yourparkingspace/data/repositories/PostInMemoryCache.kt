package com.yourparkingspace.data.repositories

import com.yourparkingspace.domain.model.Listing
import com.yourparkingspace.domain.model.Post

internal class PostInMemoryCache {

    private val cache: MutableMap<String, MutableMap<String, Listing<Post>>> = mutableMapOf()

    fun clear() = cache.clear()

    fun getListing(subredditName: String, key: String): Listing<Post>? {
        return cache[subredditName]?.get(key)
    }

    fun setListing(subredditName: String, key: String, listing: Listing<Post>) {
        if (cache[subredditName] == null) {
            cache[subredditName] = mutableMapOf()
        }

        cache[subredditName]?.set(key, listing)
    }

    fun getPost(id: String): Post? {
        cache.entries.forEach {
            val post = it.value.values.flatten().find { it.id == id }
            if (post != null) return post
        }

        return null
    }

    fun setPost(post: Post) {
        cache.values.forEach { value ->
            value.values.forEach { listing ->
                val index = listing.indexOfFirst { it.id == post.id }
                if (index >= 0) {
                    listing[index] = post
                }
            }
        }
    }
}