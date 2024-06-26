package com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanCodeDiffUtil
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI

class ScanCodesAdapter(
    private var scanList: List<ScanObjectUI> = emptyList(),
    private val onItemSelected: (ScanData) -> Unit,
    private val onDeleteItem: (Int) -> Unit,
    private val goToActionScan: (ScanData) -> Unit,
    private val onNoteItem: (String) -> Unit
) : RecyclerView.Adapter<ScanCodesViewholder>() {

        fun updateList(list:List<ScanObjectUI>){
            val diff = ScanCodeDiffUtil(scanList,list)
            val result = DiffUtil.calculateDiff(diff)
            scanList = list
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanCodesViewholder {
        return ScanCodesViewholder(LayoutInflater.from(parent.context).inflate( R.layout.item_scan_code,parent,false))
    }

    override fun getItemCount() = scanList.size

    override fun onBindViewHolder(holder: ScanCodesViewholder, position: Int) {
        holder.render(scanList[position],onItemSelected,onDeleteItem,goToActionScan,onNoteItem)
    }
}