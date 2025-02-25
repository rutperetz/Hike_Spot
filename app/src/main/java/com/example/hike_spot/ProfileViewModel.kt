package com.example.hike_spot.ui.profile

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    // משתנה לשמירת הURI של תמונת הפרופיל (זמני, עד שנוסיף Firebase)
    private var tempProfileImageUri: Uri? = null

    // פונקציה לשמירת ה-URI של התמונה
    fun saveProfileImage(uri: Uri?) {
        tempProfileImageUri = uri
    }

    // פונקציה לקבלת ה-URI השמור
    fun getProfileImageUri(): Uri? {
        return tempProfileImageUri
    }

    // כשנוסיף את Glide וFirebase נוסיף כאן פונקציות לטעינת התמונה
}