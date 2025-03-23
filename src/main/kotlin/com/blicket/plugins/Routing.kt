package com.blicket.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/templates", "templates")
        staticResources("/styles", "styles")
        staticResources("/scripts", "scripts")

        route("/") {
            get {
                call.respondRedirect("/templates/index.html")
            }

            get("messages") {
                call.respondRedirect("/templates/messages.html")
            }

            get("login") {
                call.respondRedirect("/templates/login.html")
            }

            // RESTful API ENDPOINTS
            post("new-message") {

            }

            post("get-messages") {

            }
        }
    }
}