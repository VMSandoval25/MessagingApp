package app.getsizzle.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
 data class Dietitian (
    override val fullName: String,
    override val userId: String,
    override val password: String,
    override val email: String,
    override val pfp: String? = null,
    override val role: UserRole = UserRole.DIETITIAN,
) : Users() {
    override val groupId: Int = userId.hashCode()// PK
    companion object {
         const val path = "/dietitian"
     }
 }