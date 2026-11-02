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
        // Stub: will be wired to implicit intent ACTION_DIAL in Commit 21
        Toast.makeText(requireContext(), "Call clicked: ${appointment.phone}", Toast.LENGTH_SHORT).show()
    }

    private fun handleRescheduleClick(appointment: Appointment) {
        // Stub: will be wired to reschedule dialog in Commit 23
        Toast.makeText(requireContext(), "Reschedule clicked: ${appointment.patientName}", Toast.LENGTH_SHORT).show()
    }

    private fun handleDeleteClick(appointment: Appointment) {
        // Stub: will be wired to delete confirmation in Commit 24
        Toast.makeText(requireContext(), "Delete clicked: ${appointment.patientName}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}