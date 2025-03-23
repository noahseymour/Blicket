package com.blicket.plugins

import com.blicket.model.server.Storage
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import io.ktor.server.response.respond

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }

    routing {

        post("/login") {
            val parameters = call.receiveParameters()
            val publicKey = parameters["public-key"] ?: ""
            val privateKey = parameters["private-key"] ?: ""

            // PLACEHOLDER TO CHECK KEY PAIR IS VALID

            if (Storage.contains(publicKey)) {
                call.respondRedirect("messenger")
            } else {
                call.respond(
                    ThymeleafContent("complete-profile", mapOf("publicKey" to publicKey, "privateKey" to privateKey))
                )
            }
        }
    }
}
