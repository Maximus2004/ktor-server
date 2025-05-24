package com.example.database.connects

import com.example.auth.ConnectResponse
import com.example.auth.JobResponse
import com.example.database.jobs.Jobs
import com.example.database.jobs.Jobs.projectId
import com.example.database.projects.Projects
import com.example.database.users.Users
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Connects : Table("connects") {
    private val connectId = Connects.varchar("connectid", 200)
    private val text = Connects.varchar("text", 200)
    private val userId = Connects.varchar("userid", 200).references(Users.login)
    private val jobId = Connects.varchar("jobid", 200).references(Jobs.jobId)

    fun insert(connectDTO: ConnectDTO) {
        transaction {
            Connects.insert {
                it[connectId] = UUID.randomUUID().toString()
                it[text] = connectDTO.text
                it[userId] = connectDTO.userId
                it[jobId] = connectDTO.jobId
            }
        }
    }

    fun fetchConnectById(connectId: String) = try {
        transaction {
            Connects
                .join(Jobs, JoinType.INNER, onColumn = Connects.jobId, otherColumn = Jobs.jobId)
                .join(Projects, JoinType.INNER, onColumn = Jobs.projectId, otherColumn = Projects.projectId)
                .selectAll()
                .where { Connects.connectId eq connectId }
                .singleOrNull()
                ?.let{ row ->
                    ConnectResponse(
                        connectId = row[Connects.connectId],
                        login = row[userId],
                        text = row[text]
                    )
                }
        }
    } catch (e: Exception) {
        null
    }

    fun fetchConnectsByUserId(creator: String) = try {
        transaction {
            Connects
                .join(Jobs, JoinType.INNER, onColumn = Connects.jobId, otherColumn = Jobs.jobId)
                .join(Projects, JoinType.INNER, onColumn = Jobs.projectId, otherColumn = Projects.projectId)
                .selectAll()
                .where { Projects.creator eq creator }
                .map {
                    ConnectResponse(
                        connectId = it[connectId],
                        login = it[userId],
                        text = it[text]
                    )
                }
        }
    } catch (e: Exception) {
        null
    }
}