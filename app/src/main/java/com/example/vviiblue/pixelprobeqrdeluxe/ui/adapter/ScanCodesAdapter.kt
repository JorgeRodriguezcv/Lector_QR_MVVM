package com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI

class ScanCodesAdapter(private var scanList: List<ScanObjectUI> = emptyList(), private val onItemSelected:(ScanObjectUI)->Unit) :
    RecyclerView.Adapter<ScanCodesViewholder>() {

        fun updateList(list:List<ScanObjectUI>){
            scanList = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanCodesViewholder {
        return ScanCodesViewholder(LayoutInflater.from(parent.context).inflate( R.layout.item_scan_code,parent,false))
    }

    override fun getItemCount() = scanList.size

    override fun onBindViewHolder(holder: ScanCodesViewholder, position: Int) {
        holder.render(scanList[position],onItemSelected)
    }
}