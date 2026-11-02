package com.example.appointmentmanagement.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appointmentmanagement.databinding.FragmentBookAppointmentBinding

/**
 * Fragment containing the form inputs to book a new appointment.
 */
class BookAppointmentFragment : Fragment() {

    private var _binding: FragmentBookAppointmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}