package com.example.baseproject.presenter.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.presenter.recognize.RecognizeActivity
import com.example.baseproject.presenter.recognize.RecognizeActivity.Companion.EXTRA_VERIFY_TYPE
import com.example.baseproject.presenter.register.RegisterActivity
import com.example.baseproject.util.AppEnvironment
import com.example.baseproject.util.ext.*
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity(), PermissionListener {

    private lateinit var binding: ActivityMainBinding

    private var verifyType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { openRegisterPage() }
        binding.btnCheckin.setOnClickListener {
            this.verifyType = AppEnvironment.VerifyTypes.CHECKIN
            openRecognizePageWithPermission()
        }
        binding.btnCheckout.setOnClickListener {
            this.verifyType = AppEnvironment.VerifyTypes.CHECKOUT
            openRecognizePageWithPermission()
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        response?.let {
            when (it.permissionName) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> openRecognizePageWithPermission()
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> openRecognizePageWithPermission()
                Manifest.permission.CAMERA -> openRecognizePageWithPermission()
            }
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        when (response?.permissionName) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                binding.root.showSnackbar(getString(R.string.message_storage_access_required))
            }
            Manifest.permission.CAMERA -> {
                binding.root.showSnackbar(getString(R.string.message_camera_access_required))
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        request: PermissionRequest?,
        token: PermissionToken?
    ) {
        token?.continuePermissionRequest()
    }

    private fun openRegisterPage() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun openRecognizePage() {
        val intent = Intent(this, RecognizeActivity::class.java)
            .putExtra(EXTRA_VERIFY_TYPE, verifyType)

        startActivity(intent)
    }

    private fun openRecognizePageWithPermission() {
        if (!hasReadExtStoragePermission()) {
            requestSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE, this)
        } else if (!hasWriteExtStoragePermission()) {
            requestSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
        } else if (!hasCameraPermission()) {
            requestSinglePermission(Manifest.permission.CAMERA, this)
        } else {
            openRecognizePage()
        }
    }
}