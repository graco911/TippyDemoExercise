package com.gracodev.tippy

import android.animation.ArgbEvaluator
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Color.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvPercentLabel.text = "$INITIAL_TIP_PERCENT%"

        UpdateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")

                tvPercentLabel.text = "$progress%"

                UpdateTipDescription(progress)

                ComputeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        editTextBase.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                Log.i(TAG, "afterTextChanged $s")

                ComputeTipAndTotal()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun UpdateTipDescription(tipPercent: Int) {

        val tipDescription : String

        when (tipPercent){
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great"
            else -> tipDescription = "Amazing"
        }

        textViewTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int

        textViewTipDescription.setTextColor(color)
    }

    private fun ComputeTipAndTotal() {

        //Get the value of the base and tip percent
        if (editTextBase.text.toString().isEmpty()){
            textViewTipAmount.text = ""
            textViewTotalAmount.text = ""
            return
        }
        val baseAmount = editTextBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        textViewTipAmount.text = "%.2f".format(tipAmount)
        textViewTotalAmount.text = "%.2f".format(totalAmount)

    }
}