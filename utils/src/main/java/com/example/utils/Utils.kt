package com.example.utils

import io.reactivex.rxjava3.core.Observable


@Suppress("UNCHECKED_CAST")
inline fun <reified R : Any> Observable<*>.filterIsInstance(): Observable<R> = filter { it is R } as Observable<R>