package com.yourparkingspace.domain.repository

interface IAuthenticator {

    fun isLoggedIn(): Boolean

    fun signIn(): Boolean
}