package com.example.appointmentmanagement.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appointmentmanagement.R
import com.example.appointmentmanagement.databinding.FragmentDashboardBinding
import com.example.appointmentmanagement.model.AppointmentStatus
import com.example.appointmentmanagement.repository.AppointmentRepository

/**
 * Fragment presenting system summary statistics and quick navigation actions.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationActions()
    }

    override fun onResume() {
        super.onResume()
        // Refresh summary metrics whenever returning to this screen
        refreshDashboardData()
    }

    /**
     * Wires quick action buttons to destination actions in nav_graph.xml.
     */
    private fun setupNavigationActions() {
        binding.btnQuickBook.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_bookAppointment)
        }

        binding.btnViewAll.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_myAppointments)
        }
    }

    /**
     * Reads current appointments from the singleton repository and updates dashboard cards.
     */
    private fun refreshDashboardData() {
        val allAppointments = AppointmentRepository.getAllAppointments()

        // Count appointments by status
        val bookedCount = allAppointments.count { it.status == AppointmentStatus.BOOKED }
        val completedCount = allAppointments.count { it.status == AppointmentStatus.COMPLETED }

        binding.tvBookedCount.text = bookedCount.toString()
        binding.tvCompletedCount.text = completedCount.toString()

        // Find the first upcoming booked appointment
        val nextAppointment = allAppointments.firstOrNull { it.status == AppointmentStatus.BOOKED }

        if (nextAppointment != null) {
            binding.tvNextDoctor.text = nextAppointment.doctorName
            binding.tvNextDateTime.text = "Date: ${nextAppointment.date} at ${nextAppointment.time}"
            binding.tvNextReason.text = "Reason: ${nextAppointment.reason}"
        } else {
            binding.tvNextDoctor.text = "No upcoming appointments"
            binding.tvNextDateTime.text = "Schedule a visit using the button below"
            binding.tvNextReason.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}