package com.blicket.plugins

import com.blicket.model.server.Storage
import io.ktor.http.parameters
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/templates", "templates")
        staticResources("/css", "css")
        staticResources("/scripts", "scripts")

        route("/") {
            get {
                call.respondRedirect("/templates/index.html")
            }

            get("messenger") {
                call.respondRedirect("/templates/messenger.html")
            }

            get("login") {
                call.respondRedirect("/templates/login.html")
            }

            // RESTful API Endpoints

            post("complete-profile") {
                val parameters = call.receiveParameters()
                val publicKey = parameters["publicKey"] ?: ""
                val privateKey = parameters["privateKey"] ?: ""
                val name = parameters["name"] ?: ""

                // won't throw an error because of check in post "login"
                Storage.addUser(publicKey, privateKey, name)

                call.respondRedirect("/messenger")
            }

            post("new-message") {

            }

            post("get-messages") {

            }
        }
    }
}