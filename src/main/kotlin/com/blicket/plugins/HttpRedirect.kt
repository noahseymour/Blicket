package com.blicket.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.httpsredirect.*

fun Application.configureHttpRedirect() {
    install(HttpsRedirect) {
        // The port to redirect to. By default, 443, the default HTTPS port.
        sslPort = 8443
        // 301 Moved Permanently, or 302 Found redirect.
        permanentRedirect = true
    }
}