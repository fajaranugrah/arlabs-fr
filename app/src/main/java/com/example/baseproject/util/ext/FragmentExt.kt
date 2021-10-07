package com.example.baseproject.util.ext

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener


fun Fragment.showToast(
    message: String,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(
        requireContext(), message, length
    ).show()
}

fun Fragment.getColor(res: Int): Int {
    return ContextCompat.getColor(requireContext(), res)
}

fun Fragment.showSnackbar(message: String) {
    Snackbar.make(this.requireView(), message, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.hasCameraPermission(): Boolean =
    ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasReadExtStoragePermission(): Boolean =
    ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.hasWriteExtStoragePermission(): Boolean =
    ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.requestSinglePermission(
    permission: String,
    listener: PermissionListener,
    isSameThread: Boolean = true
) {
    Dexter.withContext(requireContext())
        .withPermission(permission)
        .withListener(listener)
        .apply { if (isSameThread) onSameThread() }
        .check()
}