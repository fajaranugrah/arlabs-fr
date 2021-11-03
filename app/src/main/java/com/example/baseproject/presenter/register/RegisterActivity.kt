package com.example.baseproject.presenter.register

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import coil.clear
import coil.load
import com.example.baseproject.R
import com.example.baseproject.data.Resource
import com.example.baseproject.databinding.ActivityRegisterBinding
import com.example.baseproject.domain.model.Image
import com.example.baseproject.domain.model.User
import com.example.baseproject.presenter.MainApplication
import com.example.baseproject.presenter.dialog.ProgressDialogFragment
import com.example.baseproject.util.ext.*
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil
import pl.aprilapps.easyphotopicker.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity(), PermissionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RegisterViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityRegisterBinding

    private val progressDialog by lazy { ProgressDialogFragment.newInstance() }

    private val imagePicker by lazy {
        EasyImage.Builder(this)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViewListener()
        initObserver()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imagePicker.handleActivityResult(
            requestCode = requestCode,
            resultCode = resultCode,
            resultIntent = data,
            activity = this,
            callbacks = object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    imageFiles.firstOrNull()?.let {
                        viewModel.setUserPhotoFile(it.file)
                    }
                }
            })

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        response?.let {
            when (it.permissionName) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> showImagePickerWithPermission()
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> showImagePickerWithPermission()
                Manifest.permission.CAMERA -> showImagePickerWithPermission()
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

    private fun initViewListener() {
        binding.etNik.doAfterTextChanged {
            if (binding.tilNik.isErrorEnabled) {
                binding.tilNik.isErrorEnabled = false
            }
        }
        binding.etUsername.doAfterTextChanged {
            if (binding.tilUserName.isErrorEnabled) {
                binding.tilUserName.isErrorEnabled = false
            }
        }

        binding.cardUserPhoto.setOnClickListener { showImagePickerWithPermission() }
        binding.btnDeleteUserPhoto.setOnClickListener { viewModel.setUserPhotoFile(null) }
        binding.btnNext.setOnClickListener {
            UIUtil.hideKeyboard(this)
            registerUser()
        }
    }

    private fun initObserver() {
        viewModel.registerUserLiveData.observe(this@RegisterActivity, {
            when (it) {
                is Resource.Loading -> {
                    progressDialog.show(supportFragmentManager)
                }
                is Resource.Success -> {
                    sendScanLogs()
                    progressDialog.hide()
                    showToast(getString(R.string.message_user_registered_successfully))
                    onBackPressed()
                }
                is Resource.Error -> {
                    progressDialog.hide()

                    val errorMessage =
                        if (it.message?.contains("Generate Artifacts", true) == true)
                            getString(R.string.message_failed_to_process_photo)
                        else
                            it.message

                    showToast(errorMessage)
                }
            }
        })

        viewModel.userPhotoFileLiveData.observe(this@RegisterActivity, {
            setUserPhoto(it)
        })
    }

    private fun registerUser() {
        val user = User(
            id = binding.etNik.text.toString().trim(),
            name = binding.etUsername.text.toString().trim(),
            image = Image(
                fileName = viewModel.userPhotoFileLiveData.value?.name,
                extension = viewModel.userPhotoFileLiveData.value?.extension,
                file = viewModel.userPhotoFileLiveData.value,
            ),
        )
        var isDataValid = true

        if (user.image?.file == null) {
            binding.tvLabelOwner.apply {
                text = getString(R.string.error_your_photo_required)
                setTextColorRes(R.color.design_default_color_error)
            }
            binding.layoutHintOwner.visible()
            isDataValid = false
        }
        if (user.id.isNullOrEmpty()) {
            binding.tilNik.error = getString(R.string.error_nik_required)
            isDataValid = false
        }
        if (user.name.isNullOrEmpty()) {
            binding.tilUserName.error =
                getString(R.string.error_username_required)
            isDataValid = false
        }

        if (!isDataValid) {
            return
        }

        viewModel.registerUser(user)
        viewModel.user = user
    }

    private fun showImagePickerWithPermission() {
        if (!hasReadExtStoragePermission()) {
            requestSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE, this)
        } else if (!hasWriteExtStoragePermission()) {
            requestSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
        } else if (!hasCameraPermission()) {
            requestSinglePermission(Manifest.permission.CAMERA, this)
        } else {
            imagePicker.openCameraForImage(this)
        }
    }

    private fun setUserPhoto(file: File?) {
        if (file != null) {
            binding.imgOwner.load(file)
            binding.btnDeleteUserPhoto.visible()
            binding.imgIconOwner.gone()
            binding.layoutHintOwner.gone()
        } else {
            binding.imgOwner.clear()
            binding.btnDeleteUserPhoto.invisible()
            binding.imgIconOwner.visible()
            binding.tvLabelOwner.apply {
                text = getString(R.string.hint_front_face_view)
                setTextColorRes(R.color.colorSilver)
            }
            binding.layoutHintOwner.visible()
        }
    }

    private fun sendScanLogs() {
        try {
            //scan Logs Loket
            val date: Date = Calendar.getInstance().getTime()

            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateNow: String = df.format(date)
            viewModel.scanLogs(viewModel.user?.name, dateNow)
        } catch (e: Exception) { e.printStackTrace() }
    }
}