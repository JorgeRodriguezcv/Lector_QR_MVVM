package com.example.vviiblue.pixelprobeqrdeluxe.ui.history

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.FragmentHistoryBinding
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.LayoutViewTextFormBinding
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.LayoutViewWifiFormBinding
import com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter.ScanCodesAdapter
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.PermissionUtils
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.PermissionViewModel
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.Utils.getConfigurationWifi
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.Utils.toEditable
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.WifiUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel by activityViewModels<HistoryViewModel>()
    private val permissionViewModel by activityViewModels<PermissionViewModel>()

    private lateinit var scansAdapter: ScanCodesAdapter

    private lateinit var dialog: AlertDialog

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            permissionViewModel.setPermissionsGranted(true)
        } else {
            Toast.makeText(requireContext(), requireContext().getString(R.string.error_wifi_permission), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycleview()
        checkPermissionsAndConnect()
    }

    private fun initUI() {
        lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyViewModel.getAllScanCodes()
                historyViewModel.listScanCodes.collect() { listScans ->
                    scansAdapter.updateList(listScans)
                }
            }
        }
    }

    private fun initRecycleview() {
        scansAdapter = ScanCodesAdapter(
            onItemSelected = { itemScanSelected -> onItemSelected(itemScanSelected) },
            onDeleteItem = { itemToDelete -> deleteScanCode(itemToDelete) },
            goToActionScan = { itemScanSelected -> goToActionScan(itemScanSelected) }
        )
        binding.rvHistoryScans.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scansAdapter
        }
    }

    private fun checkPermissionsAndConnect() {
        if (PermissionUtils.hasLocationPermissions(requireContext())) {
            permissionViewModel.setPermissionsGranted(true)
        } else {
            requestPermissionLauncher.launch(PermissionUtils.WIFI_PERMISSIONS)
        }
    }

    private fun onItemSelected(dataScan: ScanData) {

        when (dataScan) {
            is ScanData.Url -> {
            }

            is ScanData.Text -> {
                val inflater = LayoutInflater.from(requireContext())
                val dialogView = inflater.inflate(R.layout.layout_view_text_form, null)
                val bindingDialog = LayoutViewTextFormBinding.bind(dialogView)
                bindingDialog.root.setBackgroundResource(R.color.accent)
                bindingDialog.root.isVisible = true
                bindingDialog.textQrCode.text = dataScan.text

                showDialog(dialogView,dataScan)
            }

            is ScanData.Wifi -> {
                val inflater = LayoutInflater.from(requireContext())
                val dialogView = inflater.inflate(R.layout.layout_view_wifi_form, null)
                val bindingDialog = LayoutViewWifiFormBinding.bind(dialogView)
                val listPartsConfigWifi = getConfigurationWifi(dataScan.wifiData)
                bindingDialog.root.setBackgroundResource(R.color.accent)
                bindingDialog.root.isVisible = true

                bindingDialog.apply {
                    textTitleNetworkName.text = "Name:"
                    textEncryption.text = listPartsConfigWifi[0].toEditable()
                    textPassword.text = listPartsConfigWifi[1].toEditable()
                    textNetworkName.text = listPartsConfigWifi[2].toEditable()
                }

                showDialog(dialogView,dataScan)
            }
        }
    }

    private fun showDialog(dialogView: View,dataScan: ScanData) {
        val builder = AlertDialog.Builder(requireContext())

        if(dataScan is ScanData.Wifi){
            builder.setView(dialogView)
                .setPositiveButton(requireContext().getString(R.string.accept_dialog_wifi)) { _, _ ->
                    connectToWifi(dataScan.wifiData)
                }
                .setNegativeButton(requireContext().getString(R.string.cancel_dialog)) { _, _ ->
                    dialog.dismiss()
                }
        }else{
            builder.setView(dialogView)
                .setNegativeButton(requireContext().getString(R.string.accept_dialog)) { _, _->
                    dialog.dismiss()
                }
        }


        dialog = builder.create()

        dialog.show()
    }


    private fun deleteScanCode(idCodeScan: String) {
            historyViewModel.deleteScan(idCodeScan)
    }


    private fun goToActionScan(dataScan: ScanData) {

        when (dataScan) {
            is ScanData.Url -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataScan.url))
                startActivity(intent)
            }

            is ScanData.Text -> {
            }

            is ScanData.Wifi -> {
                connectToWifi(dataScan.wifiData)
            }
        }
    }

    private fun connectToWifi(wifiData: String){
        if (PermissionUtils.hasLocationPermissions(requireContext())) {
            val listPartsConfigWifi = getConfigurationWifi(wifiData)
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