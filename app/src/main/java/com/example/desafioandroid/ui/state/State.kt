package com.example.desafioandroid.ui.state

/** Classe de apoio para 'settar' os estados dos dados StateFlow e SharedFlow. */
sealed class State<T>(val data: T? = null, val message: String? = null) {
    class Sucess<T>(data: T?) : State<T>(data)
    class Error<T>(message: String?, data: T? = null) : State<T>(data, message)
    class Empty<T> : State<T>()
    class Loading<T> : State<T>()
}