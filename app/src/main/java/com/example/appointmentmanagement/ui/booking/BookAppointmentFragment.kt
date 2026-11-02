package com.example.appointmentmanagement.ui.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appointmentmanagement.databinding.FragmentBookAppointmentBinding
import com.example.appointmentmanagement.model.Appointment
import com.example.appointmentmanagement.model.AppointmentStatus
import com.example.appointmentmanagement.repository.AppointmentRepository
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

        // Submit button click listener
        binding.btnBookAppointment.setOnClickListener {
            handleBookingSubmission()
        }
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

    /**
     * Validates all form inputs, creates an Appointment record,
     * and saves it to the singleton AppointmentRepository.
     */
    private fun handleBookingSubmission() {
        val patientName = binding.etPatientName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val doctor = binding.actvDoctor.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val time = binding.etTime.text.toString().trim()
        val reason = binding.etReason.text.toString().trim()

        var isValid = true

        // Validate Patient Name
        if (patientName.isEmpty()) {
            binding.tilPatientName.error = "Patient name is required"
            isValid = false
        } else {
            binding.tilPatientName.error = null
        }

        // Validate Phone Number
        if (phone.isEmpty()) {
            binding.tilPhone.error = "Phone number is required"
            isValid = false
        } else if (phone.length < 10) {
            binding.tilPhone.error = "Enter a valid phone number (at least 10 digits)"
            isValid = false
        } else {
            binding.tilPhone.error = null
        }

        // Validate Doctor Selection
        if (doctor.isEmpty()) {
            binding.tilDoctor.error = "Please select a doctor"
            isValid = false
        } else {
            binding.tilDoctor.error = null
        }

        // Validate Appointment Date
        if (date.isEmpty()) {
            binding.tilDate.error = "Please select an appointment date"
            isValid = false
        } else {
            binding.tilDate.error = null
        }

        // Validate Appointment Time
        if (time.isEmpty()) {
            binding.tilTime.error = "Please select an appointment time"
            isValid = false
        } else {
            binding.tilTime.error = null
        }

        // Validate Reason
        if (reason.isEmpty()) {
            binding.tilReason.error = "Please enter reason for consultation"
            isValid = false
        } else {
            binding.tilReason.error = null
        }

        // Halt if validation failed
        if (!isValid) return

        // Instantiate new Appointment model
        val newAppointment = Appointment(
            id = AppointmentRepository.generateNextId(),
            patientName = patientName,
            phone = phone,
            doctorName = doctor,
            date = date,
            time = time,
            reason = reason,
            status = AppointmentStatus.BOOKED
        )

        // Save to in-memory repository
        AppointmentRepository.addAppointment(newAppointment)

        Toast.makeText(
            requireContext(),
            "Appointment scheduled successfully!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}