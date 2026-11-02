package com.example.appointmentmanagement.ui.appointments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentmanagement.databinding.ItemAppointmentBinding
import com.example.appointmentmanagement.model.Appointment
import com.example.appointmentmanagement.model.AppointmentStatus

/**
 * Adapter bridging the Appointment dataset to RecyclerView item rows using Data Binding.
 */
class AppointmentAdapter(
    private var appointments: MutableList<Appointment>,
    private val onCallClick: (Appointment) -> Unit,
    private val onRescheduleClick: (Appointment) -> Unit,
    private val onCancelClick: (Appointment) -> Unit,
    private val onDeleteClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(
        val binding: ItemAppointmentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.binding.appointment = appointment

        // Hide cancel/reschedule buttons if already cancelled or completed
        val isActive = appointment.status == AppointmentStatus.BOOKED
        holder.binding.btnCancel.visibility = if (isActive) View.VISIBLE else View.GONE
        holder.binding.btnReschedule.visibility = if (isActive) View.VISIBLE else View.GONE

        holder.binding.btnCall.setOnClickListener {
            onCallClick(appointment)
        }

        holder.binding.btnReschedule.setOnClickListener {
            onRescheduleClick(appointment)
        }

        holder.binding.btnCancel.setOnClickListener {
            onCancelClick(appointment)
        }

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(appointment)
        }

        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = appointments.size

    fun updateData(newAppointments: List<Appointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        notifyDataSetChanged()
    }
}