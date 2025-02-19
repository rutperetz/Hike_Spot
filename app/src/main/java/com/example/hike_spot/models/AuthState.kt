package com.example.hike_spot.models

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    data class Success(val user: User) : AuthState()
}