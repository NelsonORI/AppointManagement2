package com.example.appointmentmanagement.ui.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentmanagement.R
import com.example.appointmentmanagement.databinding.FragmentMyAppointmentsBinding
import com.example.appointmentmanagement.model.Appointment
import com.example.appointmentmanagement.repository.AppointmentRepository
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.example.appointmentmanagement.model.AppointmentStatus
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import com.example.appointmentmanagement.databinding.DialogRescheduleBinding
import java.util.Calendar
import java.util.Locale

/**
 * Fragment displaying the list of all scheduled appointments.
 */
class MyAppointmentsFragment : Fragment() {

    private var _binding: FragmentMyAppointmentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        // Reload appointments from repository to capture newly booked or updated records
        loadAppointments()
    }

    /**
     * Initializes the RecyclerView with LinearLayoutManager and AppointmentAdapter.
     */
    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(
            appointments = mutableListOf(),
            onCallClick = { appointment ->
                handleCallClick(appointment)
            },
            onRescheduleClick = { appointment ->
                handleRescheduleClick(appointment)
            },
            onCancelClick = { appointment ->
                showCancelConfirmationDialog(appointment)
            },
            onDeleteClick = { appointment ->
                handleDeleteClick(appointment)
            }
        )

        binding.rvAppointments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appointmentAdapter
        }
    }

    /**
     * Configures button actions on the screen.
     */
    private fun setupListeners() {
        // Floating action button routes to BookAppointmentFragment
        binding.fabBookAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_myAppointments_to_bookAppointment)
        }
    }

    /**
     * Queries the singleton repository and synchronizes data with the adapter.
     */
    private fun loadAppointments() {
        val list = AppointmentRepository.getAllAppointments()
        appointmentAdapter.updateData(list)

        // Toggle empty state visibility
        if (list.isEmpty()) {
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.rvAppointments.visibility = View.GONE
        } else {
            binding.layoutEmptyState.visibility = View.GONE
            binding.rvAppointments.visibility = View.VISIBLE
        }
    }

    private fun handleCallClick(appointment: Appointment) {
        val phoneNumber = appointment.phone.trim()

        if (phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), "No phone number available", Toast.LENGTH_SHORT).show()
            return
        }

        // Create an implicit intent targeted at the telephone dialer
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        // Verify that there is an app installed capable of handling the dial intent
        if (dialIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(dialIntent)
        } else {
            // Fallback for emulators without telephony hardware or restricted environments
            startActivity(dialIntent)
        }
    }

    /**
     * Displays a custom AlertDialog to update the appointment's date and time.
     */
    private fun handleRescheduleClick(appointment: Appointment) {
        val dialogBinding = DialogRescheduleBinding.inflate(layoutInflater)
        val calendar = Calendar.getInstance()

        dialogBinding.tvRescheduleDoctorInfo.text =
            "Rescheduling visit with ${appointment.doctorName}\nCurrent: ${appointment.date} at ${appointment.time}"

        dialogBinding.etRescheduleDate.setText(appointment.date)
        dialogBinding.etRescheduleTime.setText(appointment.time)

        // Setup DatePicker on dialog date input
        dialogBinding.etRescheduleDate.setOnClickListener {
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
                    dialogBinding.etRescheduleDate.setText(formattedDate)
                    dialogBinding.tilRescheduleDate.error = null
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        // Setup TimePicker on dialog time input
        dialogBinding.etRescheduleTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val hourIn12 = if (selectedHour == 0 || selectedHour == 12) 12 else selectedHour % 12
                    val formattedTime = String.format(
                        Locale.getDefault(),
                        "%02d:%02d %s",
                        hourIn12,
                        selectedMinute,
                        amPm
                    )
                    dialogBinding.etRescheduleTime.setText(formattedTime)
                    dialogBinding.tilRescheduleTime.error = null
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Reschedule Appointment")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val newDate = dialogBinding.etRescheduleDate.text.toString().trim()
                val newTime = dialogBinding.etRescheduleTime.text.toString().trim()

                if (newDate.isEmpty() || newTime.isEmpty()) {
                    Toast.makeText(requireContext(), "Date and time cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val success = AppointmentRepository.rescheduleAppointment(
                    id = appointment.id,
                    newDate = newDate,
                    newTime = newTime
                )

                if (success) {
                    Toast.makeText(requireContext(), "Appointment rescheduled to $newDate at $newTime", Toast.LENGTH_SHORT).show()
                    loadAppointments()
                } else {
                    Toast.makeText(requireContext(), "Failed to reschedule appointment", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Prompts the user with an AlertDialog before permanently deleting an appointment.
     */
    private fun handleDeleteClick(appointment: Appointment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Appointment")
            .setMessage("Are you sure you want to permanently delete the appointment for ${appointment.patientName} with ${appointment.doctorName}?")
            .setPositiveButton("Delete") { _, _ ->
                val removed = AppointmentRepository.deleteAppointment(appointment.id)
                if (removed) {
                    Toast.makeText(requireContext(), "Appointment deleted", Toast.LENGTH_SHORT).show()
                    loadAppointments()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete appointment", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Prompts the user with a confirmation dialog before cancelling an appointment.
     */
    private fun showCancelConfirmationDialog(appointment: Appointment) {
        if (appointment.status == AppointmentStatus.CANCELLED) {
            Toast.makeText(requireContext(), "This appointment is already cancelled", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Cancel Appointment")
            .setMessage("Are you sure you want to cancel the appointment with ${appointment.doctorName} on ${appointment.date}?")
            .setPositiveButton("Yes, Cancel") { _, _ ->
                val success = AppointmentRepository.updateStatus(appointment.id, AppointmentStatus.CANCELLED)
                if (success) {
                    Toast.makeText(requireContext(), "Appointment cancelled successfully", Toast.LENGTH_SHORT).show()
                    loadAppointments()
                } else {
                    Toast.makeText(requireContext(), "Failed to update appointment", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}