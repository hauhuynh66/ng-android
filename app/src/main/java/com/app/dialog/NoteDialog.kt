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
import androidx.room.Room
import com.app.data.NoteData
import com.app.listener.NoteDialogListener
import com.app.model.AppDatabase
import com.app.model.Note
import com.app.ngn.R
import com.app.util.Format.Companion.parseDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.DecimalFormat
import java.util.*

class NoteDialog(private val dialogListener: NoteDialogListener, data: NoteData?):DialogFragment() {
    private lateinit var dp:Button
    private lateinit var tp:Button
    private lateinit var title:EditText
    private lateinit var content:EditText
    private lateinit var db: AppDatabase
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
        db = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()

        val dpListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            run {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DATE, day)
                date.setText(getText(calendar, 1))
            }
        }

        val tpListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            run{
                calendar.set(Calendar.HOUR, hour)
                calendar.set(Calendar.MINUTE, minute)
                time.setText(getText(calendar, 2))
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
                val sb:StringBuilder = StringBuilder()
                sb.append(date.text.toString(),time.text.toString())
                val note = Note(
                        title =  this.title.text.toString(),
                        content = this.content.text.toString(),
                        displayDate = parseDate(sb.toString())
                )

                try {
                    runBlocking {
                        withContext(Dispatchers.IO){
                            db.noteDAO().insert(note)
                        }
                    }
                }catch (e : Exception){
                    println(e)
                }


                dialogListener.onAdd(
                    note
                )
            }
        }).setNegativeButton("Cancel") { di, _ ->
            run {
                val sb: StringBuilder = StringBuilder()
                sb.append(date.text.toString(), time.text.toString())
                dialogListener.onCancel(
                    Note(
                        title = this.title.text.toString(),
                        content = this.content.text.toString(),
                        displayDate = parseDate(sb.toString())
                    )
                )
                di.dismiss()
            }
        }
        return builder.create()
    }

    private fun getText(cl:Calendar, mode:Int) : String {
        val sb:StringBuilder = StringBuilder()
        val f = DecimalFormat("00")
        when(mode){
            1->{
                sb.append(cl.get(Calendar.YEAR))
                sb.append("/")
                sb.append(f.format(cl.get(Calendar.MONTH)))
                sb.append("/")
                sb.append(f.format(cl.get(Calendar.DATE)))
            }
            2->{
                sb.append(f.format(cl.get(Calendar.HOUR)))
                sb.append(":")
                sb.append(f.format(cl.get(Calendar.MINUTE)))
            }

        }
        return sb.toString()
    }

}