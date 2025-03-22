package com.blicket.plugins

import com.blicket.db.Database
import com.blicket.db.UnauthorisedUserException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // Static public resources
        staticResources("/public", "public")
        staticResources("/protected", "protected")

        // Root route
        get("/") {
            call.respondRedirect("/public/templates/index.html")
        }

        // Sign-up route (outside authentication)
        post("/sign-up") {
            call.respondRedirect("public/templates/login.html")
        }

        post("/login") {
            call.respondRedirect("/protected/templates/dashboard.html")
        }
    }
}