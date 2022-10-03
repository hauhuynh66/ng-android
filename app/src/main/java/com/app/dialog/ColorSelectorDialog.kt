package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.app.ngn.R
import com.google.android.material.slider.Slider

class ColorSelectorDialog(val callback : Callback) : DialogFragment() {
    private var selectedColor : String = "#FF0000"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.dlg_color_selector, null, false)
        val redSlider = view.findViewById<Slider>(R.id.red_slider)
        val blueSlider = view.findViewById<Slider>(R.id.blue_slider)
        val greenSlider = view.findViewById<Slider>(R.id.green_slider)
        val display = view.findViewById<ConstraintLayout>(R.id.current_color)
        val code = view.findViewById<EditText>(R.id.color_code)
        
        redSlider.addOnChangeListener { slider, value, fromUser ->
            run {
                val sb = StringBuilder()
                sb.append("#")
                sb.append(String.format("%2s",value.toInt().toString(16)))
                sb.append(String.format("%2s",blueSlider.value.toInt().toString(16)))
                sb.append(String.format("%2s",greenSlider.value.toInt().toString(16)))

                selectedColor = sb.toString()

                code.setText(selectedColor)
            }
        }

        blueSlider.addOnChangeListener { slider, value, fromUser ->
            run {
                val sb = StringBuilder()
                sb.append("#")
                sb.append(String.format("%2s",value.toInt().toString(16)))
                sb.append(String.format("%2s",blueSlider.value.toInt().toString(16)))
                sb.append(String.format("%2s",greenSlider.value.toInt().toString(16)))

                selectedColor = sb.toString()

                code.setText(selectedColor)
            }
        }

        greenSlider.addOnChangeListener { slider, value, fromUser ->
            run {
                val sb = StringBuilder()
                sb.append("#")
                sb.append(String.format("%2s",value.toInt().toString(16)))
                sb.append(String.format("%2s",blueSlider.value.toInt().toString(16)))
                sb.append(String.format("%2s",greenSlider.value.toInt().toString(16)))

                selectedColor = sb.toString()

                code.setText(selectedColor)
            }
        }

        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Confirm") { di, _ ->
                run {
                    callback.onConfirm(selectedColor)
                    di.dismiss()
                }
            }.setNegativeButton("Cancel") { di, _ ->
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