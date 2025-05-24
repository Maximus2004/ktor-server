package com.example.auth

import com.example.database.users.UserDTO
import com.example.database.users.Users
import com.example.utils.isEmailValid
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

class RegisterController(val call: ApplicationCall) {
    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<SignUpRequest>()
        if (!registerReceiveRemote.email.isEmailValid()) {
            call.respond(HttpStatusCode.BadRequest, "Login is not valid")
        }
        val userDTO = Users.fetchUser(registerReceiveRemote.email)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            Users.insert(
                UserDTO(
                    email = registerReceiveRemote.email,
                    password = registerReceiveRemote.password,
                    name = registerReceiveRemote.name,
                    surname = registerReceiveRemote.surname,
                    description = registerReceiveRemote.description
                )
            )
            call.sessions.set(UserSession(login = registerReceiveRemote.email))
            call.respond(AuthResponse(registerReceiveRemote.email))
        }
    }
}