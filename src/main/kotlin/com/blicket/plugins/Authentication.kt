package com.blicket.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    install(Authentication)
}