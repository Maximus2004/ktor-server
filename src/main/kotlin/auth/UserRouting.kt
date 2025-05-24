package com.example.auth

import com.example.database.users.Users
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.get

fun Application.configureUsersRouting() {
    routing {
        get("/user/{login}") {
            val login = call.parameters["login"]
            if (login == null) {
                call.respond(HttpStatusCode.BadRequest, "Login is incorrect")
            } else {
                val user = Users.fetchUser(login)
                if (user != null) {
                    call.respond(
                        UserResponse(
                            user.name,
                            user.surname,
                            user.description,
                            user.email,
                            user.password
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Login doesn't exist")
                }
            }
        }
    }
}