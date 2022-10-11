package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.app.ngn.R
import com.google.android.material.slider.Slider

class ColorSelectorDialog(private val defaultColor : Int, val callback : Callback) : DialogFragment() {
    private var selectedColor : String = String.format("#%06X", defaultColor)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_color_selector, null, false)
        val redSlider = view.findViewById<Slider>(R.id.red_slider)
        val blueSlider = view.findViewById<Slider>(R.id.blue_slider)
        val greenSlider = view.findViewById<Slider>(R.id.green_slider)
        val display = view.findViewById<ConstraintLayout>(R.id.current_color)
        val code = view.findViewById<TextView>(R.id.color_code)

        display.setBackgroundColor(defaultColor)
        code.text = selectedColor
        redSlider.value = Color.red(defaultColor).toFloat()
        blueSlider.value = Color.blue(defaultColor).toFloat()
        greenSlider.value = Color.green(defaultColor).toFloat()

        redSlider.addOnChangeListener { _, value, _ ->
            run {
                val sb = StringBuilder()
                sb.append("#")
                val p1 = String.format("%2s",value.toInt().toString(16)).replace(" ", "0")
                val p3 = String.format("%2s",blueSlider.value.toInt().toString(16)).replace(" ", "0")
                val p2 = String.format("%2s",greenSlider.value.toInt().toString(16)).replace(" ", "0")
                sb.append(p1)
                sb.append(p2)
                sb.append(p3)

                selectedColor = sb.toString()
                code.setText(selectedColor)
                display.setBackgroundColor(Color.parseColor(selectedColor))
            }
        }

        blueSlider.addOnChangeListener { _, value, _ ->
            run {
                val sb = StringBuilder()
                sb.append("#")
                val p1 = String.format("%2s",redSlider.value.toInt().toString(16)).replace(" ", "0")
                val p3 = String.format("%2s",value.toInt().toString(16)).replace(" ", "0")
                val p2 = String.format("%2s",greenSlider.value.toInt().toString(16)).replace(" ", "0")
                sb.append(p1)
                sb.append(p2)
                sb.append(p3)

                selectedColor = sb.toString()
                code.setText(selectedColor)
                display.setBackgroundColor(Color.parseColor(selectedColor))
            }
        }

        greenSlider.addOnChangeListener { _, value, _ ->
            run {
                val sb = StringBuilder()
                sb.append("#")
                val p1 = String.format("%2s",redSlider.value.toInt().toString(16)).replace(" ", "0")
                val p3 = String.format("%2s",blueSlider.value.toInt().toString(16)).replace(" ", "0")
                val p2 = String.format("%2s",value.toInt().toString(16)).replace(" ", "0")
                sb.append(p1)
                sb.append(p2)
                sb.append(p3)

                selectedColor = sb.toString()
                code.setText(selectedColor)
                display.setBackgroundColor(Color.parseColor(selectedColor))
            }
        }

        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Confirm") { di, _ ->
                run {
                    callback.onConfirm(selectedColor)
                    di.dismiss()
                }
            }
            .setNegativeButton("Cancel") { di, _ ->
                run {
                    callback.onCancelled()
                    di.dismiss()
                }
            }

        return builder.create()
    }

    interface Callback{
        fun onConfirm(color : String)

        fun onCancelled(){

        }
    }
}