package com.app.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.app.ngn.R

abstract class EditDialog<T>(private val data: T, private val listener: Listener<T>) : DialogFragment() {
    abstract fun inflateView() : View
    abstract fun getValue(view: View) : T
    abstract fun ini(view : View, value : T)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = inflateView()
        ini(view, data)
        builder.setView(view)

        builder.setPositiveButton("Confirm"){
            di, _ -> run{
                val currentValue = getValue(view)
                println(currentValue)
                listener.onConfirm(currentValue)
                di.dismiss()
            }
        }
        builder.setNegativeButton("No"){
            di, i -> run{
                listener.onDismiss()
                di.dismiss()
            }
        }
        return builder.create()
    }

    interface Listener<T>{
        fun onConfirm(value : T)
        fun onDismiss(){

        }
    }
}

class EditTextDialog(data : String, listener: Listener<String>) : EditDialog<String>(data, listener){
    override fun inflateView(): View {
        return layoutInflater.inflate(R.layout.dlg_text_edit, null, false)
    }

    override fun getValue(view: View): String {
        val editElement = view.findViewById<EditText>(R.id.edit) ?: return ""
        println("Text evoke")
        return editElement.text.toString()
    }

    override fun ini(view: View, value: String) {
        val editElement = view.findViewById<EditText>(R.id.edit) ?: return
        editElement.setText(value)
    }
}

class NumberEditDialog(data : Number, listener: Listener<Number>, private val min : Int? = 0, private val max : Int? = 100) :
    EditDialog<Number>(data, listener){
    override fun inflateView(): View {
        return layoutInflater.inflate(R.layout.dlg_number_edit, null, false)
    }

    override fun getValue(view: View): Number {
        val editElement = view.findViewById<NumberPicker>(R.id.edit) ?: return 0
        println("Number evoke")
        return editElement.value
    }

    override fun ini(view: View, value: Number) {
        val editElement = view.findViewById<NumberPicker>(R.id.edit) ?: return
        editElement.minValue = min?:0
        editElement.maxValue = max?:100
        editElement.value = value.toInt()
    }
}