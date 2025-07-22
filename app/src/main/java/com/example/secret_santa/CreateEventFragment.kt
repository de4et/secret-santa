package com.example.secret_santa

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.secret_santa.databinding.FragmentCreateEventBinding
import com.example.secret_santa.storage.ServiceLocator
import java.util.Calendar

class CreateEventFragment : Fragment(R.layout.fragment_create_event) {

    private var viewBinding: FragmentCreateEventBinding? = null
    private var datePickerDialog: DatePickerDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCreateEventBinding.bind(view)
        initView()
    }

    private fun initView() {
        viewBinding?.apply {
            initDatePicker()
            datePickerBtn.setOnClickListener(::onDatePickerClick)
            val (year, month, day) = getTodayDate()
            datePickerBtn.text = "$day/${month + 1}/$year"
            createEventBtn.setOnClickListener(::createEvent)
        }
    }

    private fun createEvent(view: View) {
        ServiceLocator.eventStorage.add(
            name = viewBinding?.eventNameIe?.text.toString(),
            date = viewBinding?.datePickerBtn?.text.toString(),
            participants = emptyList()
        )

        findNavController().navigate(R.id.action_createEventFragment_to_mainFragment)
    }

    private fun getTodayDate(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return Triple(year, month, day)
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            viewBinding?.datePickerBtn?.text = "$dayOfMonth/${month + 1}/$year"
        }

        val (year, month, day) = getTodayDate()
        datePickerDialog = DatePickerDialog(requireContext(), dateSetListener, year, month, day)
    }

    private fun onDatePickerClick(view: View) {
        datePickerDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}