package com.blicket

import com.blicket.plugins.configureRouting
import com.blicket.plugins.configureSockets
import com.blicket.plugins.configureTemplating
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureTemplating()
    configureRouting()
    configureSockets()
}