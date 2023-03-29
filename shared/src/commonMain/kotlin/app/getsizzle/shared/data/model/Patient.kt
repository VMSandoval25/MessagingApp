package app.getsizzle.shared.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
@Serializable
data class Patient (
    override val fullName: String,
    override val userId: String,
    override val password: String,
    override val email: String,
    override val groupId: Int? = null,
    override val pfp: String? = null,
    override val role: UserRole = UserRole.PATIENT,
    val lastSentAt: Instant? = null,

    val age: Int? = null,
    val height: Int? = null, // save this in inches and cnvert to feet and inches
    val weight: Double? = null,
    val bloodPressure: String? = null,
    val allergens:String? = null,
    val gender:Gender? = Gender.UNSPECIFIED,
    val lastVisit: String? = null,
    val currentPrescriptions: String? = null,
    val notes: String? = null,

    ) : Users(){
    companion object {
        const val path = "/patient"
    }
}