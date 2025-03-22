package com.blicket

import com.blicket.plugins.configureAuthentication
import com.blicket.plugins.configureRouting
import com.blicket.plugins.configureTemplating
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.server.application.*
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.netty.EngineMain
import java.io.File


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureTemplating()
    configureRouting()
    configureAuthentication()
}

private fun ApplicationEngine.Configuration.envConfig() {

    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")
}
