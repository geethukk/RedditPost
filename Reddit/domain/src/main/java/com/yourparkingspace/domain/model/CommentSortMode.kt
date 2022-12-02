package com.yourparkingspace.domain.model

enum class CommentSortMode {
    BEST,
    NEW,
    TOP,
    CONTROVERSIAL,
    OLD,
    QA;

    companion object {
        val DEFAULT_MODE = BEST
    }
}