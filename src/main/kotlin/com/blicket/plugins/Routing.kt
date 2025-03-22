package com.blicket.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        staticResources("/css", "css")
        staticResources("/templates", "templates")
        staticResources("/scripts", "scrips")

        route("/") {
            get {
                call.respondRedirect("templates/index.html")
            }

            authenticate("auth-form") {
                post("login") {
                    call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
                }
            }
        }
    }
}
