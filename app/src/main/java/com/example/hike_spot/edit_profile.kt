package com.example.hike_spot.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hike_spot.R
import com.example.hike_spot.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private var selectedImageUri: Uri? = null
    private var currentUsername: String = ""

    // לאנצ'ר לבחירת תמונה מהגלריה
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                binding.profileImage.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        // טעינת מידע של המשתמש הנוכחי (ינוטרל בינתיים)
        loadUserData()

        // הגדרת מאזינים לכפתורים
        setupListeners()
    }

    private fun loadUserData() {
        // כרגע רק מגדירים ערכים ראשוניים לצורך הדוגמה
        // בהמשך נחליף את זה בקריאות ל-Firebase
        currentUsername = "DefaultUser"
        binding.editProfileUsername.setText(currentUsername)

        // אם יש תמונת פרופיל ברירת מחדל, היא כבר מוגדרת ב-XML
    }

    private fun setupListeners() {
        // כפתור שינוי תמונת פרופיל
        binding.changeProfileButton.setOnClickListener {
            openGallery()
        }

        // כפתור התנתקות

        binding.buttonLogout.setOnClickListener {
            // מעבר למסך EditProfileFragment
            findNavController().navigate(R.id.login)
        }
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun saveUserData() {
        val username = binding.editProfileUsername.text.toString().trim()

        if (username.isEmpty()) {
            showError("Username cannot be empty")
            return
        }

        // הצגת הודעת הצלחה
        showSuccess("Profile updated successfully")

        // שמירת המידע המעודכן (כרגע רק בזיכרון)
        currentUsername = username

        // בהמשך כאן תתווסף השמירה ל-Firebase
    }

    private fun logout() {
        // כרגע רק עוברים למסך התחברות, בהמשך נוסיף התנתקות מ-Firebase
        navigateToLogin()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.fragment_login)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // שמירת השינויים אם המשתמש יוצא מהמסך
        saveUserData()
        _binding = null
    }
}