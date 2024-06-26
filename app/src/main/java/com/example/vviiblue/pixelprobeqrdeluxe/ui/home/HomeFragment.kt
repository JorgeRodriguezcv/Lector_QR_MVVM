package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.FragmentHomeBinding
import com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter.ScanCodesAdapter
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.PermissionUtils
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.PermissionViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.SelectedItem
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.Utils.getConfigurationWifi
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.Utils.toEditable
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.WifiUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()

    //Permission controller
    private val permissionViewModel by activityViewModels<PermissionViewModel>()


    private lateinit var scansAdapter: ScanCodesAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            permissionViewModel.setPermissionsGranted(true)
        } else {
            Toast.makeText(requireContext(), "Permiso de WIFI denegado", Toast.LENGTH_LONG).show()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycleview()
        initListeners()
        initObserves()

        checkPermissionsAndConnect()
    }

    private fun checkPermissionsAndConnect() {
        if (PermissionUtils.hasLocationPermissions(requireContext())) {
            permissionViewModel.setPermissionsGranted(true)
        } else {
            requestPermissionLauncher.launch(PermissionUtils.WIFI_PERMISSIONS)
        }
    }

    private fun initUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
               // homeViewModel.getAllScanCodes()
                homeViewModel.listScanCodes.collect { listScans ->
                    withContext(Dispatchers.Main.immediate) {
                        scansAdapter.updateList(listScans)
                    }
                }

            }
        }
    }

    private fun initRecycleview() {
        scansAdapter = ScanCodesAdapter(
            onItemSelected = { itemScanSelected -> onItemSelected(itemScanSelected) },
            onDeleteItem = { itemToDelete -> deleteScanCode(itemToDelete) },
            goToActionScan = { itemScanSelected -> goToActionScan(itemScanSelected) },
            onNoteItem = { itemNoteSelected -> onNoteItem(itemNoteSelected)}
        )


        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scansAdapter
        }
    }

    private fun initListeners() {

        binding.idWebViewInclude.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                homeViewModel.onPageFinished()
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                homeViewModel.onPageFinished()
                Toast.makeText(requireContext(), "Error al cargar la página", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initObserves() {
        observePageStatus()
        observeSelectedItem()
    }

    private fun observePageStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.webViewEvent.collect { event: WebViewEvent? ->
                    when (event) {
                        is WebViewEvent.PageStarted -> binding.pb.isVisible = true
                        is WebViewEvent.PageFinished -> binding.pb.isVisible = false
                        null -> binding.pb.isVisible = false
                    }
                }
            }
        }
    }

    private fun observeSelectedItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.selectedItem.collect { selectedItem ->
                    when (selectedItem) {
                        is SelectedItem.Url -> {
                            binding.idWebViewInclude.root.isVisible = true
                            binding.idWebViewInclude.webView.loadUrl(selectedItem.url)
                        }
                        is SelectedItem.Text -> {
                            binding.idTextInclude.root.isVisible = true
                            binding.idTextInclude.textTitle.text = requireContext().getString(R.string.textCodeQR)
                            binding.idTextInclude.textContent.text = selectedItem.text.toEditable()
                        }
                        is SelectedItem.Wifi -> {
                            binding.idWifiInclude.root.isVisible = true
                            binding.idWifiInclude.apply {
                                textEncryption.text = selectedItem.encryption.toEditable()
                                textPassword.text = selectedItem.password.toEditable()
                                textNetworkName.text = selectedItem.networkName.toEditable()
                            }
                        }
                        null -> {

                        }
                    }
                }
            }
        }
    }

    private fun onItemSelected(dataScan: ScanData) {
        if (binding.idWifiInclude.root.isVisible)
            binding.idWifiInclude.root.isVisible = false

        if (binding.idTextInclude.root.isVisible)
            binding.idTextInclude.root.isVisible = false

        homeViewModel.handleSelectedItem(dataScan)

    }



    private fun deleteScanCode(idCodeScan: Int) {
            homeViewModel.deleteScan(idCodeScan)
    }

    private fun goToActionScan(dataScan: ScanData) {

        when (dataScan) {
            is ScanData.Url -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataScan.url))
                startActivity(intent)
            }

            is ScanData.Text -> {
                binding.idTextInclude.root.isVisible = true
                binding.idTextInclude.textTitle.text = requireContext().getString(R.string.textCodeQR)
                binding.idTextInclude.textContent.text = dataScan.text.toEditable()
            }

            is ScanData.Wifi -> {
                if (PermissionUtils.hasLocationPermissions(requireContext())) {
                    val listPartsConfigWifi = getConfigurationWifi(dataScan.wifiData)
                    WifiUtils.connectToWifi(
                        requireContext(),
                        listPartsConfigWifi[0],
                        listPartsConfigWifi[1],
                        listPartsConfigWifi[2]
                    )
                } else {
                    checkPermissionsAndConnect()
                }
            }
        }
    }

    private fun onNoteItem(note: String) {
        binding.idTextInclude.root.isVisible = true
        binding.idTextInclude.textTitle.text = requireContext().getString(R.string.note_title)
        binding.idTextInclude.textContent.text = note
    }

}



