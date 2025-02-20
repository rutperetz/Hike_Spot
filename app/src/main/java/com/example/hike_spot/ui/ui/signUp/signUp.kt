// SignUp.kt
package com.example.hike_spot.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hike_spot.R
import com.example.hike_spot.databinding.FragmentSignUpBinding
import com.example.hike_spot.models.AuthState
import com.example.hike_spot.ui.ui.signUp.SignUpViewModel
import com.example.hike_spot.ui.ui.signUp.SignUpViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class SignUp : Fragment() {
    private lateinit var viewModel: SignUpViewModel
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, SignUpViewModelFactory())[SignUpViewModel::class.java]
        setupClickListeners()
        observeSignUpState()
    }

    private fun setupClickListeners() {
        with(binding) {
            signUpSignUpButton.setOnClickListener {
                val name = nameSignUpEditText.text.toString()
                val email = emailSignUpEditText.text.toString()
                val password = PasswordSignUpEditText.text.toString()
                val confirmPassword = confirmSignUpEditText.text.toString()

                if (validateInput(name, email, password, confirmPassword)) {
                    viewModel.register(name, email, password)
                }
            }

            loginSignUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_sign_up_to_login)
            }
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        // בדיקת שם
        if (name.isEmpty()) {
            (binding.nameSignUpEditText.parent.parent as? TextInputLayout)?.error = "Name is required"
            isValid = false
        } else {
            (binding.nameSignUpEditText.parent.parent as? TextInputLayout)?.error = null
        }

        // בדיקת אימייל
        if (email.isEmpty()) {
            (binding.emailSignUpEditText.parent.parent as? TextInputLayout)?.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            (binding.emailSignUpEditText.parent.parent as? TextInputLayout)?.error = "Invalid email format"
            isValid = false
        } else {
            (binding.emailSignUpEditText.parent.parent as? TextInputLayout)?.error = null
        }

        // בדיקת סיסמה
        if (password.isEmpty()) {
            (binding.PasswordSignUpEditText.parent.parent as? TextInputLayout)?.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            (binding.PasswordSignUpEditText.parent.parent as? TextInputLayout)?.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            (binding.PasswordSignUpEditText.parent.parent as? TextInputLayout)?.error = null
        }

        // בדיקת אימות סיסמה
        if (confirmPassword.isEmpty()) {
            (binding.confirmSignUpEditText.parent.parent as? TextInputLayout)?.error = "Confirm password is required"
            isValid = false
        } else if (password != confirmPassword) {
            (binding.confirmSignUpEditText.parent.parent as? TextInputLayout)?.error = "Passwords do not match"
            isValid = false
        } else {
            (binding.confirmSignUpEditText.parent.parent as? TextInputLayout)?.error = null
        }

        return isValid
    }

    private fun observeSignUpState() {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> showLoading(true)
                is AuthState.Success -> {
                    showLoading(false)
                    navigateToFeed()
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
            signUpSignUpButton.isEnabled = !show
            loginSignUpButton.isEnabled = !show
            signUpProgressBar.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToFeed() {
        findNavController().navigate(R.id.action_sign_up_to_feed)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}