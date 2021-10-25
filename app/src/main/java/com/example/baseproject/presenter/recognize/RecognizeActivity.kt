package com.example.baseproject.presenter.recognize

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.baseproject.data.Resource
import com.example.baseproject.databinding.ActivityRecognizeBinding
import com.example.baseproject.domain.model.*
import com.example.baseproject.presenter.MainApplication
import com.example.baseproject.presenter.recognize.result.RecognizeResultDialogFragment
import com.example.baseproject.util.AppEnvironment
import com.example.baseproject.util.ext.*
import com.example.baseproject.util.mlkit.CameraXViewModel
import com.example.baseproject.util.mlkit.VisionImageProcessor
import com.example.baseproject.util.mlkit.facedetector.FaceDetectorProcessor
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.face.FaceDetectorOptions
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.*
import javax.inject.Inject


class RecognizeActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VERIFY_TYPE = "verify_type"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RecognizeViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityRecognizeBinding

    private val cameraExecutor by lazy { ContextCompat.getMainExecutor(this) }

    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageCaptureUseCase: ImageCapture? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var lensFacing = CameraSelector.LENS_FACING_FRONT
    private var cameraSelector: CameraSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        binding = ActivityRecognizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initCameraProvider()
        initObserver()
    }

    override fun onResume() {
        super.onResume()

        if (!viewModel.isOnProgress) {
            bindAllCameraUseCases()
        }
    }

    override fun onPause() {
        super.onPause()

        imageProcessor?.run { this.stop() }
    }

    override fun onDestroy() {
        super.onDestroy()

        imageProcessor?.run { this.stop() }
    }

    private fun initView() {
        binding.imgLogoPeduliLingdungi.load(AppEnvironment.Logo.PEDULI_LINDUNGI_COLORED)
        //binding.imgLogoRisetAi.load(AppEnvironment.Logo.RISET_AI_COLORED)
        binding.imgLogoAsriLiving.load(AppEnvironment.Logo.ASRI_LIVING)
    }

    private fun initCameraProvider() {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(CameraXViewModel::class.java)
            .processCameraProvider
            .observe(
                this,
                { provider: ProcessCameraProvider? ->
                    cameraProvider = provider
                    bindAllCameraUseCases()
                }
            )
    }

    private fun initObserver() {
        viewModel.thermal.observe(this, {
            // Nothing to do
        })

        viewModel.identifyCheckInLiveData.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showProgress(true)
                }
                is Resource.Success -> {
                    showProgress(false)
                    it.data?.let { data ->
                        showRecognizeResultDialog(
                            type = AppEnvironment.VerifyTypes.CHECKIN,
                            checkIn = data,
                            thermal = viewModel.thermal.value,
                        )
                    }
                }
                is Resource.Error -> {
                    showProgress(false)
                    showToast(it.message)

                    Handler().postDelayed({
                        viewModel.isOnProgress = false
                    }, 3000)
                }
            }
        })

        viewModel.identifyCheckOutLiveData.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showProgress(true)
                }
                is Resource.Success -> {
                    showProgress(false)
                    it.data?.let { data ->
                        showRecognizeResultDialog(
                            type = AppEnvironment.VerifyTypes.CHECKOUT,
                            checkOut = data,
                            thermal = viewModel.thermal.value,
                        )
                    }
                }
                is Resource.Error -> {
                    showProgress(false)
                    showToast(it.message)

                    Handler().postDelayed({
                        viewModel.isOnProgress = false
                    }, 3000)
                }
            }
        })
    }

    private fun showRecognizeResultDialog(
        type: String,
        checkIn: CheckIn? = null,
        checkOut: CheckOut? = null,
        thermal: Thermal? = null,
    ) {
        RecognizeResultDialogFragment.newInstance(
            type = type,
            checkIn = checkIn,
            checkOut = checkOut,
            thermal = thermal,
        ).apply {
            onDismiss = {
                Handler().postDelayed({
                    viewModel.isOnProgress = false
                }, 2000)
            }
        }.show(supportFragmentManager, RecognizeResultDialogFragment.TAG)
    }

    private fun recognizePhoto(imageFile: File) {
        val exif = ExifInterface(imageFile.inputStream())
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val bitmap = BitmapFactory.decodeStream(imageFile.inputStream())
        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }

        lifecycleScope.launch {
            try {
                showProgress(true)

                val compressedImageFile =
                    Compressor.compress(this@RecognizeActivity, imageFile) {
                        size(maxFileSize = 1500 * 1024) // 1.5MB
                    }

                val user = User(
                    image = Image(
                        file = compressedImageFile
                    )
                )

                when (intent.getStringExtra(EXTRA_VERIFY_TYPE)) {
                    AppEnvironment.VerifyTypes.CHECKIN -> viewModel.identifyCheckIn(user)
                    AppEnvironment.VerifyTypes.CHECKOUT -> viewModel.identifyCheckOut(user)
                }
            } catch (e: FileNotFoundException) {
                showProgress(false)
                Timber.e(e)
                showToast("Gambar tidak dapat ditemukan")
            } catch (e: IOException) {
                showProgress(false)
                Timber.e(e)
                showToast("Gagal mendapatkan gambar")
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            binding.tvProgressHint.visible()
            binding.progressHorizontal.visible()
        } else {
            binding.tvProgressHint.gone()
            binding.progressHorizontal.gone()
        }
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            cameraProvider?.unbindAll()
            bindPreviewUseCase()
            bindImageCaptureUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider?.unbind(previewUseCase)
        }

        previewUseCase = Preview.Builder().build()
        previewUseCase?.setSurfaceProvider(binding.previewView.surfaceProvider)

        cameraProvider?.bindToLifecycle(this, cameraSelector!!, previewUseCase)
    }

    private fun bindImageCaptureUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (imageCaptureUseCase != null) {
            cameraProvider?.unbind(imageCaptureUseCase)
        }

        imageCaptureUseCase = ImageCapture.Builder().build()

        cameraProvider?.bindToLifecycle(this, cameraSelector!!, imageCaptureUseCase)
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider?.unbind(analysisUseCase)
        }
        if (imageProcessor != null) {
            imageProcessor?.stop()
        }
        imageProcessor =
            try {
                val faceDetectorOptions = FaceDetectorOptions.Builder()
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setMinFaceSize(1f)
                FaceDetectorProcessor(this, faceDetectorOptions.build()).apply {
                    onFaceDetected = {
                        if (!viewModel.isOnProgress) {
                            viewModel.isOnProgress = true
                            takePicture()
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                return
            }

        analysisUseCase = ImageAnalysis.Builder().build()
        analysisUseCase?.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            cameraExecutor,
            { imageProxy: ImageProxy ->
                val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                    binding.graphicOverlay.setImageSourceInfo(
                        imageProxy.width,
                        imageProxy.height,
                        isImageFlipped
                    )
                } else {
                    binding.graphicOverlay.setImageSourceInfo(
                        imageProxy.height,
                        imageProxy.width,
                        isImageFlipped
                    )
                }

                try {
                    imageProcessor?.processImageProxy(imageProxy, binding.graphicOverlay)
                } catch (e: MlKitException) {
                    Timber.e("Failed to process image. Error: %s", e.localizedMessage)
                    showToast(e.localizedMessage)
                }
            }
        )
        cameraProvider?.bindToLifecycle(this, cameraSelector!!, analysisUseCase)
    }

    private fun takePicture() {
        val imageFile = File(getBatchDirectoryName(), "LastCapturedImage" + ".jpg")

        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(imageFile)
                .setMetadata(ImageCapture.Metadata())
                .build()
        imageCaptureUseCase?.takePicture(
            outputFileOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    showToast("Failed to save image")
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    recognizePhoto(imageFile)
                }
            })
    }

    private fun getBatchDirectoryName(): String {
        val appFolderPath =
            Environment.getExternalStorageDirectory().toString() + "/Pictures/ImageRecognitionTrial"
        val dir = File(appFolderPath)

        if (!dir.exists() && !dir.mkdirs()) {
            // Nothing to do
        }

        return appFolderPath
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)

        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun Bitmap.toInputStream(): InputStream {
        val bos = ByteArrayOutputStream()

        compress(CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)

        val bitmapdata: ByteArray = bos.toByteArray()
        return ByteArrayInputStream(bitmapdata)
    }
}