package com.example.appointmentmanagement.ui.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentmanagement.databinding.ItemAppointmentBinding
import com.example.appointmentmanagement.model.Appointment

/**
 * Adapter bridging the Appointment dataset to RecyclerView item rows using Data Binding.
 *
 * @property appointments Mutable collection of appointments to be rendered.
 * @property onCallClick Callback invoked when user selects the Call action.
 * @property onRescheduleClick Callback invoked when user selects the Reschedule action.
 * @property onDeleteClick Callback invoked when user selects the Delete action.
 */
class AppointmentAdapter(
    private var appointments: MutableList<Appointment>,
    private val onCallClick: (Appointment) -> Unit,
    private val onRescheduleClick: (Appointment) -> Unit,
    private val onDeleteClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    /**
     * ViewHolder caching references to row item bindings.
     */
    inner class AppointmentViewHolder(
        val binding: ItemAppointmentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        // Inflate item layout using generated Data Binding class
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]

        // Assign the model instance directly to the Data Binding variable in item_appointment.xml
        holder.binding.appointment = appointment

        // Attach action click listeners
        holder.binding.btnCall.setOnClickListener {
            onCallClick(appointment)
        }

        holder.binding.btnReschedule.setOnClickListener {
            onRescheduleClick(appointment)
        }

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(appointment)
        }

        // Force immediate evaluation of bound expressions
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = appointments.size

    /**
     * Replaces the adapter's backing list with fresh data and notifies the UI.
     */
    fun updateData(newAppointments: List<Appointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        notifyDataSetChanged()
    }
}