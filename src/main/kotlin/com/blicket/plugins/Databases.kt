package com.blicket.plugins

import com.blicket.db.Database
import io.ktor.server.application.*


fun Application.configureDatabases() {
    // Get the database file path from configuration or use a default
    val dbPath = environment.config.propertyOrNull("database.path")?.getString()
        ?: "/Users/noahseymour/Documents/Kotlin/Imperial/Blicket/src/main/resources/Users"

// Then use this path to construct your JDBC URL
    val jdbcUrl = "jdbc:sqlite:$dbPath"

    Database.init(dbPath)
}