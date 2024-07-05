package com.example.vviiblue.pixelprobeqrdeluxe.ui.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.FragmentScanBinding
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.ui.barcode.QrCodeAnalyzer
import com.example.vviiblue.pixelprobeqrdeluxe.ui.barcode.view.BarcodeBoxView
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScanFragment : Fragment() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeBoxView: BarcodeBoxView

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private val scanViewModel by viewModels<ScanViewModel>()

    companion object {
        private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
        private const val TAG = "BarcodeScannerActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission() //Permission is requested
    ) { isGranted -> // Indicates whether the permission was granted or not (true o false)
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.accept_permission),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            requireContext(),
            CAMERA_PERMISSION
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkCameraPermission()) {
            //Permissions have been accepted
            startCamera()
        } else {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }

        initUI()

    }

    private fun initUI() {
        initScanCamera()
        initListeners()
    }

    private fun initScanCamera() {
        barcodeBoxView = BarcodeBoxView(requireContext())
        /** The 'barcodeBoxView' view is added to the scan fragment and is specified to
         *  take all the space of its parent (fragmentScan) */
        (view as ViewGroup).addView(
            barcodeBoxView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun initListeners() {
        binding.fabExitScanCam.setOnClickListener {
            exitScanCam()
        }
    }

    private fun exitScanCam() {
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // I get the available camera
            val cameraProvider = cameraProviderFuture.get()

            // I configured the preview view
            val preview = Preview.Builder().build().also { preview ->
                preview.setSurfaceProvider(binding.rvCameraScan.surfaceProvider)
            }

            // Set up image analysis to detect barcodes
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor, QrCodeAnalyzer(
                            requireContext(),
                            barcodeBoxView,
                            binding.rvCameraScan.width.toFloat(),
                            binding.rvCameraScan.height.toFloat()
                        ) { result , note-> barCodeReaded(result,note) }
                    )

                }

            // choose the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unite preview and image analysis
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Error starting the camera: ${exc.message}", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun barCodeReaded(result: String, note: String) {

        lifecycleScope.launch {
            val scanObjectUI = ScanObjectUI(-1, result.trim(), "...", note)
            scanViewModel.insertScanCode(scanObjectUI)
        }
        /** I close the scan */
        exitScanCam()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}