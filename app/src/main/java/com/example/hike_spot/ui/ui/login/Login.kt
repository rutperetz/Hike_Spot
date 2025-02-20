// Login.kt
package com.example.hike_spot.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hike_spot.R
import com.example.hike_spot.databinding.FragmentLoginBinding
import com.example.hike_spot.models.AuthState
import com.example.hike_spot.ui.ui.login.LoginViewModel
import com.example.hike_spot.ui.ui.login.LoginViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class Login : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeLoginState()
    }

    private fun setupClickListeners() {
        with(binding) {
            loginLoginButton.setOnClickListener {
                val email = emailLoginTextView.text.toString()
                val password = passwordLoginEditText.text.toString()

                if (validateInput(email, password)) {
                    viewModel.login(email, password)
                }
            }

            loginSignUpButton.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_login_to_sign_up)
                } catch (e: Exception) {
                    showError("Navigation error: ${e.message}")
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        // בדיקת אימייל
        if (email.isEmpty()) {
            (binding.emailLoginTextView.parent.parent as? TextInputLayout)?.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            (binding.emailLoginTextView.parent.parent as? TextInputLayout)?.error = "Invalid email format"
            isValid = false
        } else {
            (binding.emailLoginTextView.parent.parent as? TextInputLayout)?.error = null
        }

        // בדיקת סיסמה
        if (password.isEmpty()) {
            (binding.passwordLoginEditText.parent.parent as? TextInputLayout)?.error = "Password is required"
            isValid = false
        } else {
            (binding.passwordLoginEditText.parent.parent as? TextInputLayout)?.error = null
        }

        return isValid
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> showLoading(true)
                is AuthState.Success -> {
                    showLoading(false)
                    try {
                        findNavController().navigate(R.id.action_login_to_feed)
                    } catch (e: Exception) {
                        showError("Navigation error: ${e.message}")
                    }
                }
                is AuthState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
                else -> showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            loginLoginButton.isEnabled = !show
            loginSignUpButton.isEnabled = !show
            loginProgressBar.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
