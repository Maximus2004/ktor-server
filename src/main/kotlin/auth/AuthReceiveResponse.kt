package com.example.auth

import kotlinx.serialization.Serializable

// Requests
@Serializable
data class SignUpRequest(
    val name: String,
    val surname: String,
    val description: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class ProjectRequest(
    val name: String,
    val description: String
)

@Serializable
data class JobRequest(
    val name: String,
    val description: String
)

@Serializable
data class ConnectRequest(
    val login: String,
    val text: String
)

// Responses
@Serializable
data class UserResponse(
    val name: String,
    val surname: String,
    val description: String,
    val email: String,
    val password: String
)

@Serializable
data class ProjectResponse(
    val projectId: String,
    val name: String,
    val description: String
)

@Serializable
data class ConnectResponse(
    val connectId: String,
    val login: String,
    val text: String
)

@Serializable
data class JobResponse(
    val jobId: String,
    val name: String,
    val description: String,
    val publishDate: String,
    val projectId: String
)

@Serializable
data class AuthResponse(
    val login: String
)

// Sessions
@Serializable
data class UserSession(val login: String)