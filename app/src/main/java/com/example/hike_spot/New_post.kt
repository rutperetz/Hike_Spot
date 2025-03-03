package com.example.yourapp // Replace with your actual package name

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hike_spot.R
import com.example.hike_spot.databinding.FragmentEditProfileBinding
import com.example.hike_spot.databinding.FragmentNewPostBinding
import de.hdodenhof.circleimageview.CircleImageView

class NewPostFragment : Fragment() {

    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var usernameTextView: TextView
    private lateinit var profileImageView: CircleImageView
    private lateinit var postEditText: EditText
    private lateinit var locationGroup: RadioGroup
    private lateinit var addPhotoButton: Button
    private lateinit var postButton: Button

    private var selectedLocation: String = ""
    private var selectedImageUri: Uri? = null

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        usernameTextView = view.findViewById(R.id.username_newpost)
        profileImageView = view.findViewById(R.id.profileImage)
        postEditText = view.findViewById(R.id.editpost_editText)
        locationGroup = view.findViewById(R.id.locationGroup)
        addPhotoButton = view.findViewById(R.id.add_photo_button)
        postButton = view.findViewById(R.id.post_button)

        // Set initial user data (replace with actual user data when you have Firebase)
        setUserData("Username") // Placeholder username

        // Set up location selection
        setupLocationSelection()

        // Set up click listeners
        setupClickListeners()
    }

    private fun setUserData(username: String) {
        usernameTextView.text = username
        // Set profile image from resources for now
        profileImageView.setImageResource(R.drawable.user)
    }


    private fun setupListeners() {
        binding.postButton.setOnClickListener {
           // מעבר למסך EditProfileFragment
            findNavController().navigate(R.id.feed)
       }
    }


    private fun setupLocationSelection() {
        locationGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedLocation = when (checkedId) {
                R.id.cb_center -> "Center"
                R.id.cb_north -> "North"
                R.id.cb_south -> "South"
                R.id.cb_lowlands -> "Lowlands"
                else -> ""
            }
        }
    }

    private fun setupClickListeners() {
        addPhotoButton.setOnClickListener {
            openGallery()
        }

        postButton.setOnClickListener {
            createPost()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                // Here you would typically display a preview of the selected image
                // For now, just show a success message
                Toast.makeText(requireContext(), "Image selected successfully", Toast.LENGTH_SHORT).show()

                // Change button text to indicate image is selected
                addPhotoButton.text = "Change Photo"
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPost() {
        val postText = postEditText.text.toString().trim()

        // Validate input
        if (postText.isEmpty() && selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please add text or image to your post", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedLocation.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a location", Toast.LENGTH_SHORT).show()
            return
        }

        // Create post object (will be used with Firebase later)
        val post = Post(
            id = System.currentTimeMillis().toString(),
            userId = "current_user_id",
            username = usernameTextView.text.toString(),
            text = postText,
            location = selectedLocation,
            imageUrl = selectedImageUri?.toString() ?: "",
            timestamp = System.currentTimeMillis()
        )

        // For now, just show success message and reset form
        Toast.makeText(requireContext(), "Post created successfully!", Toast.LENGTH_LONG).show()
        resetForm()
        findNavController().navigate(R.id.feed)

        // Later you'll save this post to Firebase
        // When you implement Firebase, you'll call a method here to save the post
    }

    private fun resetForm() {
        postEditText.text.clear()
        locationGroup.clearCheck()
        selectedLocation = ""
        selectedImageUri = null
        addPhotoButton.text = "Add Photo"
    }

    // This class will be used later with Firebase
    data class Post(
        val id: String = "",
        val userId: String = "",
        val username: String = "",
        val text: String = "",
        val location: String = "",
        val imageUrl: String = "",
        val timestamp: Long = 0
    )

}