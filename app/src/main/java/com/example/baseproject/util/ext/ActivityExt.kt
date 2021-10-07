package com.example.baseproject.util.ext

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener

fun Activity.hasCameraPermission(): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.hasReadExtStoragePermission(): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.hasWriteExtStoragePermission(): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.requestSinglePermission(
    permission: String,
    listener: PermissionListener,
    isSameThread: Boolean = true
) {
    Dexter.withContext(this)
        .withPermission(permission)
        .withListener(listener)
        .apply { if (isSameThread) onSameThread() }
        .check()
}

fun Activity.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
    }
}