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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vviiblue.pixelprobeqrdeluxe.ui.barcode.QrCodeAnalyzer
import com.example.vviiblue.pixelprobeqrdeluxe.ui.barcode.view.BarcodeBoxView
import com.example.vviiblue.pixelprobeqrdeluxe.ui.history.HistoryFragment
import com.example.vviiblue.pixelprobeqrdeluxe.ui.history.HistoryViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.home.HomeFragment
import com.example.vviiblue.pixelprobeqrdeluxe.ui.home.HomeViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

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
        ActivityResultContracts.RequestPermission() //ActivityResultContracts.RequestPermission() : aqui se solicita el permiso
    ) { isGranted -> // isGranted: indica si el permiso fue otorgado o no (true o false)
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                requireContext(),
                "Acepta los permisos",
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
            //Tiene permisos aceptados
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
        /**  Se agrega la vista "barcodeBoxView" en el fragmen scan y se le especifica que tome todo el espacio del padre (fragmentScan)*/
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
            // Cámara disponible
            val cameraProvider = cameraProviderFuture.get()

            // Configurar la vista previa
            val preview = Preview.Builder().build().also { preview ->
                preview.setSurfaceProvider(binding.rvCameraScan.surfaceProvider)
            }

            // Configurar el análisis de imagen para detectar códigos de barras
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
                        ) { result -> barCodeReaded(result) }
                    )

                }

            // Seleccionar la cámara trasera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unir la vista previa y el análisis de imagen
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Error al iniciar la cámara: ${exc.message}", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun barCodeReaded(result: String) {
        /** guardo lo que obtuve */
        lifecycleScope.launch {
            val scanObjectUI = ScanObjectUI("-1", result.trim(), "s", "f")
            scanViewModel.insertScanCode(scanObjectUI)
        }
        /** cierro el scan */
        exitScanCam()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}