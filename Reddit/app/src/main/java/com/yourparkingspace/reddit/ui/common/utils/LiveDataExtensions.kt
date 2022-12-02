package com.yourparkingspace.reddit.ui.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun MutableLiveData<*>.notifyObservers() {
    this.value = this.value
}

fun <T> MutableLiveData<T>.postNotify() {
    value?.let { postValue(it) }
}

fun <T> MutableLiveData<T>.immutable() = this as LiveData<T>