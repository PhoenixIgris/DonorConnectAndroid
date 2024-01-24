package com.buuzz.donorconnect.utils.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.utils.helpers.AppData.BLANK_IMAGE
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun TextInputEditText.onTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            afterTextChanged.invoke(s.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
        }

    })
}

fun Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.getTempUriForCamera(): Uri {
    val tempImagesDir = File(
        applicationContext.filesDir, //this function gets the external cache dir
        getString(R.string.temp_image)
    ) //gets the directory for the temporary images dir
    tempImagesDir.mkdir()
    //Creates the temp_image.jpg file
    val tempImage = File(
        tempImagesDir, //prefix the new abstract path with the temporary images dir path
        getString(R.string.temp_image) + System.currentTimeMillis()
    ) //gets the abstract temp_image file name
    //Returns the Uri object to be used with ActivityResultLauncher
    return FileProvider.getUriForFile(
        applicationContext,
        getString(R.string.authorities),
        tempImage
    )
}

fun Context.isPhotoPickerAvailable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            true
        }
    } else {
        false
    }
}

internal fun convertUriToBase64(context: Context?, uri: Uri): String? {
    var encImage = ""
    try {
        val inputStream = context?.contentResolver?.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b = baos.toByteArray()
        encImage = Base64.encodeToString(b, Base64.DEFAULT)
    } catch (e: java.lang.Exception) {
        return BLANK_IMAGE
    }
    return encImage
}

fun formatDate(originalDate: String): String {
    // Parse the original date string
    val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.parse(originalDate)
    } else {
        return ""
    }

    // Format the date in the desired format
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy").withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}


