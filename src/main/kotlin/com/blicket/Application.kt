package com.blicket

import com.blicket.plugins.configureAuthentication
import com.blicket.plugins.configureHttpRedirect
import com.blicket.plugins.configureRouting
import com.blicket.plugins.configureTemplating
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureTemplating()
    configureAuthentication()
    configureHttpRedirect()
    configureRouting()
}
