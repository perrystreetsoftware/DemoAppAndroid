package com.example

fun Any.readJsonToString(fileName: String): String =
    javaClass.getResource(fileName)?.readText() ?: throw NullPointerException("File $fileName not found")

inline fun <reified T> readJsonToStringSpek(fileName: String): String =
    T::class.java.getResource(fileName)?.readText() ?: throw NullPointerException("File $fileName not found")
