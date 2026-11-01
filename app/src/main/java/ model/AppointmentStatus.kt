package com.example.appointmentmanagementsystem.model

import java.io.Serializable

/**
 * Represents the current lifecycle state of an appointment.
 *
 * Implements Serializable so it can be passed within an Appointment object
 * across fragments using Jetpack Navigation Safe Args.
 *
 * @property displayName Human-readable text suitable for displaying in the UI.
 */
enum class AppointmentStatus(val displayName: String) : Serializable {
    // Active scheduled appointment
    BOOKED("Booked"),

    // Patient attended the consultation
    COMPLETED("Completed"),

    // Appointment was called off
    CANCELLED("Cancelled")
}