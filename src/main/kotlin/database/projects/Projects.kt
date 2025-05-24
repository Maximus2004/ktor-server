package com.example.database.projects

import com.example.auth.JobResponse
import com.example.auth.ProjectResponse
import com.example.database.jobs.Jobs
import com.example.database.users.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Projects : Table("projects") {
    val creator = Projects.varchar("creator", 200).references(Users.login)
    val projectId = Projects.varchar("projectid", 200)
    private val name = Projects.varchar("name", 25)
    private val description = Projects.varchar("description", 200)

    fun insert(projectDTO: ProjectDTO) {
        transaction {
            Projects.insert {
                it[projectId] = UUID.randomUUID().toString()
                it[name] = projectDTO.name
                it[description] = projectDTO.description
                it[creator] = projectDTO.creator
            }
        }
    }

    fun fetchProjectById(projectId: String) = try {
        transaction {
            Projects.selectAll().where { Projects.projectId eq projectId }.singleOrNull()?.let {
                ProjectResponse(
                    projectId = it[Projects.projectId],
                    name = it[name],
                    description = it[description],
                )
            }
        }
    } catch(e: Exception) {
        null
    }

    fun fetchProjectsByCreator(creator: String) =
        try {
            transaction {
                Projects.selectAll().where { Projects.creator eq creator }.map {
                    ProjectResponse(
                        projectId = it[projectId],
                        name = it[name],
                        description = it[description],
                    )
                }
            }
        } catch(e: Exception) {
            listOf()
        }
}