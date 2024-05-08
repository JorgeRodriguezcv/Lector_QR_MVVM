package com.example.vviiblue.pixelprobeqrdeluxe.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.FragmentHomeBinding
import com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter.ScanCodesAdapter
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private lateinit var scansAdapter: ScanCodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycleview()
        initListeners()
        observePageStatus()
    }

    private fun initUI() {
        lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getAllScanCodes()
                homeViewModel.listScanCodes.collect() { listScans ->
                    scansAdapter.updateList(listScans)
                }

            }
        }
    }

    private fun initRecycleview() {
        scansAdapter = ScanCodesAdapter(
            onItemSelected = { itemScanSelected -> onItemSelected(itemScanSelected) },
            onDeleteItem = { itemToDelete -> deleteScanCode(itemToDelete) },
            onGoToWeb = { itemScanSelected -> goToWebScan(itemScanSelected) }
        )


        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scansAdapter
        }
    }

    private fun initListeners() {

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                // Ocultar la barra de progreso
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

    private fun observePageStatus() {
        lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    private fun onItemSelected(dataScan: ScanData) {
        if(binding.idWifiInclude.root.isVisible)
            binding.idWifiInclude.root.isVisible = false

        if(binding.idTextInclude.root.isVisible)
            binding.idTextInclude.root.isVisible = false


        when(dataScan){
            is ScanData.Url -> {
                homeViewModel.onPageStarted()
                binding.webView.loadUrl(dataScan.url)
            }
            is ScanData.Text -> {
                binding.idTextInclude.root.isVisible = true
                binding.idTextInclude.textQrCode.text = dataScan.text.toEditable()
            }
            is ScanData.Wifi -> {
                showConfigurationWifi(dataScan.wifiData)
                binding.idWifiInclude.root.isVisible = true
            }
        }
    }

    private fun showConfigurationWifi(wifiData: String) {
        val parts: List<String> = wifiData.split(";")
        var ssid: String = ""
        var password: String = ""
        var securityType: String = ""
        for (part in parts) {
            if (part.startsWith("S:")) {
                ssid = part.substring(2)
            } else if (part.startsWith("P:")) {
                password = part.substring(2)
            } else if (part.startsWith("WIFI")) {
                val _securityType = part.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[2]
                securityType = _securityType
            }
        }

        binding.idWifiInclude.textEncryption.text = securityType.toEditable()
        binding.idWifiInclude.textPassword.text = password.toEditable()
        binding.idWifiInclude.textNetworkName.text = ssid.toEditable()
    }

    private fun deleteScanCode(idCodeScan: String) {
        lifecycleScope.launch() {
            homeViewModel.deleteScan(idCodeScan)
        }
    }

    private fun goToWebScan(dataScan: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataScan))
        startActivity(intent)
    }



}


/** Función de extensión para convertir un String a Editable */
private fun String.toEditable(): Editable {
  return  Editable.Factory.getInstance().newEditable(this)
}
