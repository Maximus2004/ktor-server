package com.example.auth

import com.example.database.connects.ConnectDTO
import com.example.database.connects.Connects
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Application.configureConnectsRouting() {
    routing {
        get("/user/{login}/connects") {
            val login = call.parameters["login"]
            if (login != null) {
                val respond = Connects.fetchConnectsByUserId(login)
                if (respond != null) {
                    call.respond(respond)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "There are no connects")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Login is incorrect")
            }
        }
        get("/connect/{connectId}") {
            val connectId = call.parameters["connectId"]
            if (connectId != null) {
                val respond = Connects.fetchConnectById(connectId)
                if (respond != null) {
                    call.respond(respond)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Connect ID is incorrect")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Connect ID is incorrect")
            }
        }
        post("/job/{jobId}/connect") {
            val jobId = call.parameters["jobId"]
            if (jobId != null) {
                val connect = call.receive<ConnectRequest>()
                Connects.insert(
                    ConnectDTO(
                        userId = connect.login,
                        jobId = jobId,
                        text = connect.text
                    )
                )
            }
            else {
                call.respond(HttpStatusCode.BadRequest, "Job ID is incorrect")
            }
        }
    }
}