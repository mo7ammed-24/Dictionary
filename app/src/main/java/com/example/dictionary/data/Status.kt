package com.example.dictionary.data

sealed class Status<out T> {
    data class Loading(val loadingMessage:String): Status<Nothing>()
    data class Error(val errorMessage:String): Status<Nothing>()
    data class Success<T>(val data:T): Status<T>()
}
