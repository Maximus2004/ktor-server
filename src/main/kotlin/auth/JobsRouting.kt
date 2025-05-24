package com.example.auth

import com.example.database.jobs.JobDTO
import com.example.database.jobs.Jobs
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.time.LocalDate
import java.util.UUID

fun Application.configureJobsRouting() {
    routing {
        get("/jobs") {
            call.respond(Jobs.fetchAllJobs())
        }
        get("/project/{projectId}/jobs") {
            val projectId = call.parameters["projectId"]
            if (projectId != null) {
                call.respond(Jobs.fetchJobsByProjectId(projectId))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Project ID is incorrect")
            }
        }
        get("/job/{jobId}") {
            val jobId = call.parameters["jobId"]
            if (jobId != null) {
                val job = Jobs.fetchJobById(jobId)
                if (job != null) {
                    call.respond(job)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Job doesn't exist")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Job ID is incorrect")
            }
        }
        post("/project/{projectId}/job") {
            val projectId = call.parameters["projectId"] ?: "superproject"
            val job = call.receive<JobRequest>()
            Jobs.insert(
                JobDTO(
                    jobId = UUID.randomUUID().toString(),
                    projectId = projectId,
                    publishDate = LocalDate.now().toString(),
                    description = job.description,
                    name = job.name
                )
            )
        }
    }
}