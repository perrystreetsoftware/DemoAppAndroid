package com.example.domainmodels
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerStatusDTO(
    @Json(name="ok")
    val ok: Boolean,
    @Json(name="uptime")
    val uptime: Int,
) {
    companion object {
        val EMPTY = ServerStatusDTO(ok = false, uptime = 0)
    }
}