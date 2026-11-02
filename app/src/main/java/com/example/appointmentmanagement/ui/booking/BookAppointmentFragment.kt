package com.example.appointmentmanagement.ui.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.appointmentmanagement.databinding.FragmentBookAppointmentBinding
import java.util.Calendar
import java.util.Locale

/**
 * Fragment containing the form inputs to book a new appointment.
 */
class BookAppointmentFragment : Fragment() {

    private var _binding: FragmentBookAppointmentBinding? = null
    private val binding get() = _binding!!

    // Available doctors list for the exposed dropdown
    private val availableDoctors = arrayOf(
        "Dr. Alice Smith (General Physician)",
        "Dr. Peter Kamau (Cardiologist)",
        "Dr. Mercy Wanjiku (Dermatologist)",
        "Dr. David Otieno (Pediatrician)",
        "Dr. Grace Muthoni (Orthopedic)"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDoctorDropdown()
        setupDateAndTimePickers()
    }

    /**
     * Populates the AutoCompleteTextView exposed dropdown with doctor names.
     */
    private fun setupDoctorDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            availableDoctors
        )
        binding.actvDoctor.setAdapter(adapter)
    }

    /**
     * Attaches DatePickerDialog and TimePickerDialog to input fields.
     */
    private fun setupDateAndTimePickers() {
        val calendar = Calendar.getInstance()

        // Date Picker Dialog setup
        binding.etDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    binding.etDate.setText(formattedDate)
                    binding.tilDate.error = null
                },
                year,
                month,
                day
            )
            // Prevent picking past dates
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        // Time Picker Dialog setup
        binding.etTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val hourIn12Format = if (selectedHour == 0 || selectedHour == 12) 12 else selectedHour % 12
                    val formattedTime = String.format(
                        Locale.getDefault(),
                        "%02d:%02d %s",
                        hourIn12Format,
                        selectedMinute,
                        amPm
                    )
                    binding.etTime.setText(formattedTime)
                    binding.tilTime.error = null
                },
                hour,
                minute,
                false
            )
            timePicker.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}