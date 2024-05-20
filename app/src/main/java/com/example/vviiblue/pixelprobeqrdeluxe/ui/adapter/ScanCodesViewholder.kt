package com.example.vviiblue.pixelprobeqrdeluxe.ui.adapter

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.ItemScanCodeBinding
import com.example.vviiblue.pixelprobeqrdeluxe.ui.core.ScanData
import com.example.vviiblue.pixelprobeqrdeluxe.ui.model.ScanObjectUI

class ScanCodesViewholder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemScanCodeBinding.bind(view)
    private val context = binding.tvTitleScanItem.context

    private val defaultColor = Color.WHITE
    private val selectedColor = ContextCompat.getColor(context, R.color.secondary)


    fun render(
        scanObject: ScanObjectUI,
        onItemSelected: (ScanData) -> Unit,
        onDeleteItem: (Int) -> Unit,
        goToActionScan: (ScanData) -> Unit,
        onNoteItem: (String) -> Unit
    ) {
        var scanData:ScanData

        if (scanObject.scanCode.startsWith("http://") || scanObject.scanCode.startsWith("https://")) {
            binding.ivActionScanItem.setImageResource(R.drawable.ic_go_internet)
            scanData = ScanData.Url(scanObject.scanCode)
        } else {
            if (isWifiValid(scanObject.scanCode)) {
                binding.ivActionScanItem.setImageResource(R.drawable.ic_wifi)
                scanData = ScanData.Wifi(scanObject.scanCode)
            } else {
                binding.ivActionScanItem.setImageResource(R.drawable.ic_plane_text)
                scanData = ScanData.Text(scanObject.scanCode)
            }
        }

        binding.tvTitleScanItem.text = scanObject.scanDate
            binding.tvSubTitleScanItem.text = if (scanObject.scanCode.length > 80) {
            scanObject.scanCode.substring(0, 30) + "..."
        } else {
            scanObject.scanCode
        }

        binding.llScanContain.setOnClickListener {
            startChangeColorAnimation(
                binding.viewItemId,
                exectLambdaForItemSelected = {
                    onItemSelected(scanData)
                })
        }

        binding.ivDeleteScanItem.setOnClickListener {
            onDeleteItem(scanObject.scanIdCode)
        }

        binding.ivActionScanItem.setOnClickListener {
            goToActionScan(scanData)
        }

        binding.ivScanCodeNote.setOnClickListener {
            onNoteItem(scanObject.scanNote)
        }

    }


    private fun startChangeColorAnimation(
        selectedView: View,
        exectLambdaForItemSelected: () -> Unit
    ) {

        val animator = ValueAnimator.ofObject(ArgbEvaluator(), defaultColor, selectedColor)
        animator.duration = 500
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            selectedView.setBackgroundColor(valueAnimator.animatedValue as Int)
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                exectLambdaForItemSelected()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        animator.start()

    }


    fun isWifiValid(configuracion: String): Boolean {
        val regex = Regex("^WIFI:T:(.*);S:(.*);P:(.*);H:(.*);;$")

        return regex.matches(configuracion.trim())
    }


}