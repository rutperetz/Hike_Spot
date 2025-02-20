package com.example.hike_spot.ui.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hike_spot.models.AuthState
import com.example.hike_spot.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _signUpState = MutableLiveData<AuthState>(AuthState.Initial)
    val signUpState: LiveData<AuthState> = _signUpState

    fun register(name: String, email: String, password: String) {
        _signUpState.value = AuthState.Loading
        viewModelScope.launch {
            _signUpState.value = repository.registerUser(name, email, password)
        }
    }
}