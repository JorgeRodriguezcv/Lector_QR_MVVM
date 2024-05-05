package com.example.vviiblue.pixelprobeqrdeluxe.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.FragmentHistoryBinding
import com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter.ScanCodesAdapter
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel by activityViewModels<HistoryViewModel>()

    private lateinit var scansAdapter:ScanCodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycleview()
    }

    private fun initUI() {


       lifecycleScope.launch(){
           repeatOnLifecycle(Lifecycle.State.STARTED){
               historyViewModel.listScanCodes.collect(){
                   listScans -> scansAdapter.updateList(listScans)
               }
           }
       }


    }

    private fun initRecycleview() {
        scansAdapter = ScanCodesAdapter(){itemScanSelected -> onItemSelected(itemScanSelected)}
        binding.rvHistoryScans.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scansAdapter
        }
    }

    private fun onItemSelected(scanObjectUI: ScanObjectUI){
        //Si se trata de una url analizo si es segura sino muestro mensaje de cudiado
        // si es un usuario y contraseña para un wefi consulto si conectar
        //si es texto plano muestro texto plano



    }

}