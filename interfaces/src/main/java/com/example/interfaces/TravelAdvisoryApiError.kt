package com.example.interfaces

import java.net.HttpURLConnection

sealed class TravelAdvisoryApiError(val statusCode: Int? = null) : Throwable() {
    object CountryNotFound : TravelAdvisoryApiError(
        HttpURLConnection.HTTP_NOT_FOUND
    )

    object Forbidden : TravelAdvisoryApiError(
        HttpURLConnection.HTTP_FORBIDDEN
    )

    // These two cases cannot be pulled into a superclass, otherwise we could not enumerate
    // all possible cases of SmsSendError
    //
    // Swift allows us to use generics with the `Error` protocol; Java prohibits us from using
    // generic subclass of Throwable:
    // https://stackoverflow.com/questions/501277/
    data class CommonError(
        val apiResponseCode: ApiResponseCode,
    ) : TravelAdvisoryApiError(apiResponseCode.statusCode)

    data class Other(val throwable: Throwable) : TravelAdvisoryApiError()

    companion object {
        private fun domainErrorFromStatusCode(statusCode: Int): TravelAdvisoryApiError? {
            // https://ivanmorgillo.com/2020/03/11/can-i-loop-over-a-kotlin-sealed-class/
            return TravelAdvisoryApiError::class.sealedSubclasses
                .firstOrNull { it.objectInstance?.statusCode == statusCode }
                ?.objectInstance
        }

        fun fromStatusCode(statusCode: Int): TravelAdvisoryApiError {
            return domainErrorFromStatusCode(statusCode) ?: CommonError(
                ApiResponseCode.fromStatusCode(statusCode)
            )
        }
    }
}