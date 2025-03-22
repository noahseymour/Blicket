package com.blicket.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.*
import io.ktor.server.response.respondRedirect

fun Application.configureAuthentication() {
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                UserIdPrincipal("Valid")
            }
            challenge {
                call.respondRedirect("public/templates/login.html")
            }
        }
    }
}