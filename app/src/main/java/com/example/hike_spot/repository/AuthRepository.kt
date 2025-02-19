package com.example.hike_spot.repository

import com.example.hike_spot.models.AuthState
import com.example.hike_spot.models.User
import java.util.UUID

class AuthRepository {
    private val users = mutableListOf<User>()

    fun loginUser(email: String, password: String): AuthState {
        return try {
            val user = users.find { it.email == email && it.password == password }
            if (user != null) {
                AuthState.Success(user)
            } else {
                AuthState.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Unknown error occurred")
        }
    }

    fun registerUser(name: String, email: String, password: String): AuthState {
        return try {
            if (users.any { it.email == email }) {
                AuthState.Error("Email already exists")
            } else {
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    password = password
                )
                users.add(newUser)
                AuthState.Success(newUser)
            }
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Unknown error occurred")
        }
    }
}
