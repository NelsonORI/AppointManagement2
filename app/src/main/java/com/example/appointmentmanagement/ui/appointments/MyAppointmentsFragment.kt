package com.example.appointmentmanagement.ui.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.appointmentmanagement.databinding.FragmentMyAppointmentsBinding

/**
 * Fragment displaying the list of all scheduled appointments.
 */
class MyAppointmentsFragment : Fragment() {

    private var _binding: FragmentMyAppointmentsBinding? = null
    private val binding get() = _binding!!

    // Retrieve arguments safely via Safe Args delegate
    private val args: MyAppointmentsFragmentArgs by navArgs()

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

        // Check if navigated with a newly booked appointment
        val newlyBooked = args.newAppointment
        if (newlyBooked != null) {
            Toast.makeText(
                requireContext(),
                "Showing newly booked appointment for ${newlyBooked.patientName}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}