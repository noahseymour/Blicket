package com.blicket.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.*
import io.ktor.server.response.respondRedirect

fun Application.configureAuthentication() {
    install(Authentication) {

        basic("auth-basic") {
            realm = "Access to the '...' path"
            validate { credentials ->
                UserIdPrincipal("authenticated")
            }
        }

        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (credentials.name == "admin" && credentials.password == "password") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("login.html")
            }
        }
    }
}