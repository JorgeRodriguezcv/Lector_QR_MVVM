package com.example.vviiblue.pixelprobeqrdeluxe.ui.core

import androidx.recyclerview.widget.DiffUtil
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI

class ScanCodeDiffUtil(private val oldList:List<ScanObjectUI>, private val newList:List<ScanObjectUI>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int  = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].scanIdCode == newList[newItemPosition].scanIdCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}