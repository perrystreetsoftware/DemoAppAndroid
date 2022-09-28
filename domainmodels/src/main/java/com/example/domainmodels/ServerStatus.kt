package com.example.domainmodels

data class ServerStatus(val success: Boolean) {
    companion object {
        val EMPTY = ServerStatus(false)
    }
}