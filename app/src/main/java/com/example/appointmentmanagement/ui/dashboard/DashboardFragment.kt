package com.example.appointmentmanagement.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appointmentmanagement.databinding.FragmentDashboardBinding

/**
 * Fragment presenting system summary statistics and quick navigation actions.
 */
class DashboardFragment : Fragment() {

    // Backing field for View Binding lifecycle safety
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

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear binding reference to prevent memory leaks when the view is destroyed
        _binding = null
    }
}