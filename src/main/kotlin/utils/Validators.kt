package com.example.utils

fun String.isEmailValid() =
    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex().matches(this)