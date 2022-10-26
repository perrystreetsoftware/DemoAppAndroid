package com.example.interfaces

import java.net.HttpURLConnection

sealed class ApiResponseCode(val statusCode: Int) {
    // These should only be from HttpURLConnection.*

    object BadRequest : ApiResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
    object NotAuthorized : ApiResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
    object PaymentRequired : ApiResponseCode(HttpURLConnection.HTTP_PAYMENT_REQUIRED)
    object Forbidden : ApiResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
    object NotFound : ApiResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
    object MethodNotAllowed : ApiResponseCode(HttpURLConnection.HTTP_BAD_METHOD)
    object MethodNotAcceptable : ApiResponseCode(HttpURLConnection.HTTP_NOT_ACCEPTABLE)
    object RequestTimeout : ApiResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
    object Conflict : ApiResponseCode(HttpURLConnection.HTTP_CONFLICT)
    object TooMany : ApiResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)

    object ServerError : ApiResponseCode(500)
    object NotImplemented : ApiResponseCode(501)
    object BadGateway : ApiResponseCode(502)
    object LoadBalancerError : ApiResponseCode(503)

    data class Unknown(val innerStatusCode: Int) : ApiResponseCode(innerStatusCode)

    companion object {
        fun fromStatusCode(statusCode: Int): ApiResponseCode {
            return ApiResponseCode::class.sealedSubclasses
                .firstOrNull { it.objectInstance?.statusCode == statusCode }
                ?.objectInstance ?: Unknown(statusCode)
        }
    }
}