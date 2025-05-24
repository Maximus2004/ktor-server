package com.example.database.users

import com.example.auth.UserResponse
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val login = Users.varchar("login", 25)
    val name = Users.varchar("name", 25)
    val surname = Users.varchar("surname", 25)
    private val description = Users.varchar("description", 200)
    private val password = Users.varchar("password", 25)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.email
                it[password] = userDTO.password
                it[name] = userDTO.name
                it[surname] = userDTO.surname
                it[description] = userDTO.description
            }
        }
    }

    fun fetchUser(login: String) = try {
            transaction {
                Users.selectAll().where { Users.login eq login }.singleOrNull()?.let {
                    UserResponse(
                        email = it[Users.login],
                        name = it[name],
                        surname = it[surname],
                        description = it[description],
                        password = it[password]
                    )
                }
            }
        } catch(e: Exception) {
            null
        }
}