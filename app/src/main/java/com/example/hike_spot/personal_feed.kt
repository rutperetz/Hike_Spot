package com.example.hike_spot.ui.personal_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hike_spot.R
import com.example.hike_spot.databinding.FragmentPersonalFeedBinding

class PersonalFeedFragment : Fragment() {

    private var _binding: FragmentPersonalFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // יצירת קשר עם ה-XML של העיצוב
        _binding = FragmentPersonalFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // הגדרת מאזין ללחיצה על כפתור ההגדרות (settings_profile_button)
        binding.settingsProfileButton.setOnClickListener {
            // מעבר למסך EditProfileFragment
            findNavController().navigate(R.id.edit_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
