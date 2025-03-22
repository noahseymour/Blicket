package com.blicket.model.blockchain

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

suspend fun latestTick(): String {
    val page: String = makeGetRequest("$BASE_URL/v1/latestTick")
    return Json.decodeFromString<Map<String, JsonObject>>(page)["latestTick"].toString() //TODO POTENTIAL ERROR POINT
}

suspend fun makeGetRequest(url: String): String {
    val client = HttpClient(CIO)
    return client.use {
        val response: HttpResponse = it.get(url)
        response.bodyAsText()
    }
}

suspend fun makePostRequest(url: String, body: ByteArray): HttpResponse {
    val client = HttpClient(CIO);
    return client.use {
        val response = it.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        response
    }
}