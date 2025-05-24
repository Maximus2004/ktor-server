package com.example.auth

import com.example.database.projects.ProjectDTO
import com.example.database.projects.Projects
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Application.configureProjectsRouting() {
    routing {
        get("/user/{login}/projects") {
            val login = call.parameters["login"]
            if (login != null) {
                call.respond(Projects.fetchProjectsByCreator(login))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Login doesn't exist")
            }
        }
        get("/project/{projectId}") {
            val projectId = call.parameters["projectId"]
            if (projectId != null) {
                val project = Projects.fetchProjectById(projectId)
                if (project != null) {
                    call.respond(project)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Project doesn't exist")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Project ID is incorrect")
            }
        }
        post("/user/{login}/project") {
            val login = call.parameters["login"] ?: "superuser"
            val project = call.receive<ProjectRequest>()
            Projects.insert(
                ProjectDTO(
                    creator = login,
                    name = project.name,
                    description = project.description
                )
            )
        }
    }
}