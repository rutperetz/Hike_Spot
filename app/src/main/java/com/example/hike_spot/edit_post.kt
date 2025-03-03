import android.app.Activity
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.hike_spot.R
import de.hdodenhof.circleimageview.CircleImageView

class EditPostFragment : Fragment() {

    // UI Components
    private lateinit var titleEditPost: TextView
    private lateinit var profileImage: CircleImageView
    private lateinit var usernameTextView: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var locationGroup: RadioGroup
    private lateinit var centerRadioButton: RadioButton
    private lateinit var northRadioButton: RadioButton
    private lateinit var southRadioButton: RadioButton
    private lateinit var lowsRadioButton: RadioButton
    private lateinit var addPhotoButton: Button
    private lateinit var postButton: Button

    // Variables for data
    private var selectedLocation: String = "Center" // Default location
    private var selectedImageUri: Uri? = null
    private var originalDescription: String = ""
    private var originalLocation: String = ""

    // Result launcher for image picking
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                profileImage.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_post , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        initializeViews(view)

        // Set up listeners
        setupListeners()

        // Load existing data (you would typically get this from your ViewModel or arguments)
        loadExistingData()
    }

    private fun initializeViews(view: View) {
        titleEditPost = view.findViewById(R.id.titleEditPost)
        profileImage = view.findViewById(R.id.profileImage)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        locationGroup = view.findViewById(R.id.locationGroup)
        centerRadioButton = view.findViewById(R.id.cb_center)
        northRadioButton = view.findViewById(R.id.cb_north)
        southRadioButton = view.findViewById(R.id.cb_south)
        lowsRadioButton = view.findViewById(R.id.cb_lows)
        addPhotoButton = view.findViewById(R.id.add_photo_button)
        postButton = view.findViewById(R.id.rpost_button)
    }

    private fun setupListeners() {
        // Location radio group listener
        locationGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedLocation = when (checkedId) {
                R.id.cb_center -> "Center"
                R.id.cb_north -> "North"
                R.id.cb_south -> "South"
                R.id.cb_lows -> "Lows"
                else -> "Center" // Default
            }
        }

        // Add photo button listener
        addPhotoButton.setOnClickListener {
            openGallery()
        }

        // Post button listener
        postButton.setOnClickListener {
            saveChanges()
        }
    }

    private fun loadExistingData() {
        // בשלב זה נטען את הנתונים הקיימים של הפוסט - בדרך כלל מ-ViewModel או מארגומנטים
        // סתם לצורך הדוגמה, אני יוצר כאן נתונים קבועים
        usernameTextView.text = "JohnDoe123"

        // נניח שיש לנו את הנתונים הבאים שהועברו לפרגמנט
        arguments?.let { args ->
            // originalDescription = args.getString("description", "")
            // originalLocation = args.getString("location", "Center")

            // בנתיים אני שם ערכים לדוגמה
            originalDescription = "This is my original post description"
            originalLocation = "North"

            // מילוי הטופס בערכים הקיימים
            descriptionEditText.setText(originalDescription)

            // בחירת המיקום המקורי
            when (originalLocation) {
                "Center" -> centerRadioButton.isChecked = true
                "North" -> northRadioButton.isChecked = true
                "South" -> southRadioButton.isChecked = true
                "Lows" -> lowsRadioButton.isChecked = true
            }

            selectedLocation = originalLocation
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun saveChanges() {
        val newDescription = descriptionEditText.text.toString().trim()

        // בדיקת תקינות
        if (newDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // כאן אתה יכול לשמור את השינויים - בצורה כזו או אחרת בהתאם לארכיטקטורה של האפליקציה שלך
        // לדוגמה, אם אתה משתמש ב-ViewModel:
        // viewModel.updatePost(newDescription, selectedLocation, selectedImageUri)

        // לצורך הדוגמה פשוט נציג הודעה
        Toast.makeText(
            requireContext(),
            "Post updated successfully",
            Toast.LENGTH_SHORT
        ).show()

        // חזרה למסך הקודם
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        // פונקציה נוחה ליצירת מופע חדש של הפרגמנט עם ארגומנטים
        fun newInstance(postId: String): EditPostFragment {
            val fragment = EditPostFragment()
            val args = Bundle()
            args.putString("postId", postId)
            fragment.arguments = args
            return fragment
        }
    }
}