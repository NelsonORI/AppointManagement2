package com.example.appointmentmanagement.model

import java.io.Serializable

/**
 * Data model representing a clinic appointment.
 *
 * Implements Serializable so full appointment instances can be safely passed
 * across destinations using Jetpack Navigation Safe Args.
 *
 * @property id Unique identifier for the appointment (e.g., generated timestamp).
 * @property patientName Full name of the patient seeking consultation.
 * @property phone Contact phone number of the patient.
 * @property doctorName Name of the doctor assigned to the appointment.
 * @property date Scheduled date (formatted as DD/MM/YYYY).
 * @property time Scheduled time (formatted as HH:MM).
 * @property reason Description or chief complaint for the clinic visit.
 * @property status Current booking state, defaulted to BOOKED.
 */
data class Appointment(
    val id: Long,
    var patientName: String,
    var phone: String,
    var doctorName: String,
    var date: String,
    var time: String,
    var reason: String,
    var status: AppointmentStatus = AppointmentStatus.BOOKED
) : Serializable