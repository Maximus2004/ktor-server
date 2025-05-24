package com.example

import com.example.auth.UserSession
import com.example.auth.configureLoginRouting
import com.example.auth.configureRegisterRouting
import com.example.auth.configureConnectsRouting
import com.example.auth.configureJobsRouting
import com.example.auth.configureProjectsRouting
import com.example.auth.configureUsersRouting
import com.example.database.users.Users
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/teams",
        "org.postgresql.Driver",
        "postgres",
        "1234"
    )
    install(Sessions) {
        cookie<UserSession>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600
        }
    }
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
    configureUsersRouting()
    configureProjectsRouting()
    configureJobsRouting()
    configureConnectsRouting()
}
