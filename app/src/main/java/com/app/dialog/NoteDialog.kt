package com.app.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.app.listener.NoteDialogListener
import com.app.model.Note
import com.app.ngn.R
import com.app.util.Formatter.Companion.parseDate
import com.app.util.Utils
import java.util.*

class NoteDialog(private val dialogListener: NoteDialogListener):DialogFragment() {
    private lateinit var dp:Button
    private lateinit var tp:Button
    private lateinit var title:EditText
    private lateinit var content:EditText
    private var calendar: Calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val v = inflater.inflate(R.layout.dlg_newnote, null, false)
        this.dp = v.findViewById(R.id.showDp)
        this.tp = v.findViewById(R.id.showTp)
        this.title = v.findViewById(R.id.title)
        this.content = v.findViewById(R.id.content)
        val date:EditText = v.findViewById(R.id.date)
        val time:EditText = v.findViewById(R.id.time)

        val dpListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            run {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DATE, day)
                date.setText(Utils.getText(calendar, 1, '/'))
            }
        }

        val tpListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            run{
                calendar.set(Calendar.HOUR, hour)
                calendar.set(Calendar.MINUTE, minute)
                time.setText(Utils.getText(calendar, 2, '/'))
            }
        }

        dp.setOnClickListener {
            DatePickerDialog(requireContext(), dpListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            ).show()
        }

        tp.setOnClickListener {
            TimePickerDialog(requireContext(), tpListener,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        builder.setView(v)
        builder.setPositiveButton("Add", DialogInterface.OnClickListener{
            _, _ -> run{
                /*validate*/
                if(title.length()>10 && content.length()>10){
                    val sb:StringBuilder = StringBuilder()
                    sb.append(date.text.toString(),time.text.toString())
                    dialogListener.onAdd(
                        Note(
                            title =  this.title.text.toString(),
                            content = this.content.text.toString(),
                            displayDate = parseDate(sb.toString(), "yyyy/MM/dd HH:mm"),
                            null
                        )
                    )
                }

            }
        }).setNegativeButton("Cancel") { di, _ ->
            run {
                val sb: StringBuilder = StringBuilder()
                sb.append(date.text.toString(), time.text.toString())
                dialogListener.onCancel(
                    Note(
                        title = this.title.text.toString(),
                        content = this.content.text.toString(),
                        displayDate = parseDate(sb.toString(), "yyyy/MM/dd HH:mm")
                    )
                )
                di.dismiss()
            }
        }
        return builder.create()
    }

}