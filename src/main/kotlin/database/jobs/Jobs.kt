package com.example.database.jobs

import com.example.auth.JobResponse
import com.example.database.projects.ProjectDTO
import com.example.database.projects.Projects
import com.example.database.projects.Projects.references
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Jobs : Table("jobs") {
    val jobId = Jobs.varchar("jobid", 200)
    private val name = Jobs.varchar("name", 25)
    private val description = Jobs.varchar("description", 200)
    private val publishDate = Jobs.varchar("publishdate", 25)
    val projectId = Jobs.varchar("projectid", 200).references(Projects.projectId)

    fun insert(jobDTO: JobDTO) {
        transaction {
            Jobs.insert {
                it[jobId] = jobDTO.jobId
                it[name] = jobDTO.name
                it[description] = jobDTO.description
                it[publishDate] = jobDTO.publishDate
                it[projectId] = jobDTO.projectId
            }
        }
    }

    fun fetchJobById(jobId: String) = try {
        transaction {
            Jobs.selectAll().where { Jobs.jobId eq jobId }.singleOrNull()?.let {
                JobResponse(
                    jobId = it[Jobs.jobId],
                    projectId = it[projectId],
                    name = it[name],
                    description = it[description],
                    publishDate = it[publishDate]
                )
            }
        }
    } catch(e: Exception) {
        null
    }

    fun fetchJobsByProjectId(projectId: String) =
        try {
            transaction {
                Jobs.selectAll().where { Jobs.projectId eq projectId }.map {
                    JobResponse(
                        jobId = it[jobId],
                        name = it[name],
                        description = it[description],
                        publishDate = it[publishDate],
                        projectId = it[Jobs.projectId]
                    )
                }
            }
        } catch(e: Exception) {
            listOf()
        }

    fun fetchAllJobs() =
        try {
            transaction {
                Jobs.selectAll().map {
                    JobResponse(
                        jobId = it[jobId],
                        name = it[name],
                        description = it[description],
                        publishDate = it[publishDate],
                        projectId = it[projectId]
                    )
                }
            }
        } catch(e: Exception) {
            listOf()
        }

    fun fetchJobsBySearchRequest(request: String) =
        try {
            transaction {
                Jobs.selectAll().where { name like "%$request%" }.map {
                    JobResponse(
                        jobId = it[jobId],
                        name = it[name],
                        description = it[description],
                        publishDate = it[publishDate],
                        projectId = it[projectId]
                    )
                }
            }
        } catch(e: Exception) {
            listOf()
        }
}