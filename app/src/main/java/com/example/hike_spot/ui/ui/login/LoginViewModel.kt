package com.example.hike_spot.ui.ui.login
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hike_spot.models.AuthState
import com.example.hike_spot.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableLiveData<AuthState>(AuthState.Initial)
    val loginState: LiveData<AuthState> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            _loginState.value = repository.loginUser(email, password)
        }
    }
}