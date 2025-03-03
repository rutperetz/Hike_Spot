package com.example.hike_spot

import java.io.Serializable

data class Post(
    val id: String,
    val username: String,
    val description: String,
    val location: String,
    val photoUrl: String,
    val timestamp: Long
) : Serializable