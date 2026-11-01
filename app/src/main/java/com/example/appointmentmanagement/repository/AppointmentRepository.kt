package com.example.appointmentmanagementsystem.repository

import com.example.appointmentmanagementsystem.model.Appointment
import com.example.appointmentmanagementsystem.model.AppointmentStatus

/**
 * Singleton repository managing appointment records in memory.
 *
 * Serves as the single source of truth for CRUD operations across the app
 * prior to introducing persistent database storage.
 */
object AppointmentRepository {

    // Internal mutable list holding active and historic appointments
    private val appointments = mutableListOf<Appointment>()

    init {
        // Seed initial sample data to facilitate immediate testing in UI lists
        seedInitialData()
    }

    /**
     * Seeds the repository with realistic sample records.
     */
    private fun seedInitialData() {
        appointments.add(
            Appointment(
                id = 1L,
                patientName = "Jane Doe",
                phone = "0712345678",
                doctorName = "Dr. Alice Smith (General Physician)",
                date = "15/11/2026",
                time = "09:30 AM",
                reason = "Routine annual wellness checkup",
                status = AppointmentStatus.BOOKED
            )
        )
        appointments.add(
            Appointment(
                id = 2L,
                patientName = "John Kimani",
                phone = "0722334455",
                doctorName = "Dr. Peter Kamau (Cardiologist)",
                date = "18/11/2026",
                time = "02:00 PM",
                reason = "Follow-up blood pressure evaluation",
                status = AppointmentStatus.BOOKED
            )
        )
        appointments.add(
            Appointment(
                id = 3L,
                patientName = "Brian Ochieng",
                phone = "0733445566",
                doctorName = "Dr. Mercy Wanjiku (Dermatologist)",
                date = "01/10/2026",
                time = "11:00 AM",
                reason = "Allergic skin rash consultation",
                status = AppointmentStatus.COMPLETED
            )
        )
    }

    /**
     * Returns a read-only snapshot copy of all appointments.
     * Returning a toList() copy prevents external code from modifying the list directly.
     */
    fun getAllAppointments(): List<Appointment> {
        return appointments.toList()
    }

    /**
     * Adds a newly created appointment to the collection.
     */
    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
    }

    /**
     * Finds an appointment by its unique id.
     */
    fun getAppointmentById(id: Long): Appointment? {
        return appointments.find { it.id == id }
    }

    /**
     * Updates an existing appointment by replacing it in the collection.
     * Returns true if updated successfully, false if the record was not found.
     */
    fun updateAppointment(updatedAppointment: Appointment): Boolean {
        val index = appointments.indexOfFirst { it.id == updatedAppointment.id }
        return if (index != -1) {
            appointments[index] = updatedAppointment
            true
        } else {
            false
        }
    }

    /**
     * Updates only the status of a specific appointment.
     */
    fun updateStatus(id: Long, newStatus: AppointmentStatus): Boolean {
        val target = getAppointmentById(id)
        return if (target != null) {
            target.status = newStatus
            true
        } else {
            false
        }
    }

    /**
     * Deletes an appointment from memory by matching its unique ID.
     */
    fun deleteAppointment(id: Long): Boolean {
        return appointments.removeIf { it.id == id }
    }

    /**
     * Generates a unique ID for new records using current system time.
     */
    fun generateNextId(): Long {
        return System.currentTimeMillis()
    }
}