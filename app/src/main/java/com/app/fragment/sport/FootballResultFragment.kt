package com.app.fragment.sport

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.FootballResultAdapter
import com.app.ngn.R
import com.app.util.Utils
import com.app.viewmodel.Sport
import java.util.*

class FootballResultFragment : Fragment() {
    private val stateModel : Sport by activityViewModels()
    private val calendar : Calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_football_result_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner>(R.id.league)
        val spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.fbl, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        view.findViewById<Button>(R.id.date_picker_actions).apply {
            setOnClickListener {
                val dpListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    calendar.set(Calendar.YEAR, y)
                    calendar.set(Calendar.MONTH, m)
                    calendar.set(Calendar.DATE, d)
                    stateModel.selectedDate.value = Utils.getText(calendar, 1, '-')
                }
                DatePickerDialog(requireContext(), dpListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
                ).show()
            }
        }

        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        this.stateModel.results.observe(requireActivity()){
            list.adapter = FootballResultAdapter(requireContext(), this.stateModel.results.value!!)
        }
    }
}