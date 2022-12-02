package com.yourparkingspace.data.repositories

import com.yourparkingspace.domain.repository.IAuthenticator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class Authenticator @Inject constructor() : IAuthenticator {

    // TODO add real authentication

    private var isLoggedIn = false

    override fun isLoggedIn(): Boolean {
        return isLoggedIn
    }

    override fun signIn(): Boolean {
        isLoggedIn = true
        return true
    }
}