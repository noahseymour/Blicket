package com.blicket.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        staticResources("/css", "css")
        staticResources("/templates", "templates")
        staticResources("/scripts", "scrips")

        get("/") {
            call.respondRedirect("templates/index.html")
        }
    }
}
