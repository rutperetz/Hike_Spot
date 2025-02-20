package com.example.hike_spot.models
import com.example.hike_spot.models.User

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user :User) : AuthState()
    data class Error(val message: String) : AuthState()
}