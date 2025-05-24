package com.example.database.jobs

data class JobDTO(
    val jobId: String,
    val name: String,
    val description: String,
    val publishDate: String,
    val projectId: String
)