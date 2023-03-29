package app.getsizzle.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
abstract class Users {
    abstract val fullName: String
    abstract val userId: String     // should be unique, check database
    abstract val email: String      // regex, make sure there's an @, .com,
    abstract val password: String   // regex too
    abstract val groupId: Int?
    abstract val role: UserRole
    abstract val pfp: String?
    companion object {
        const val path = "/users"
    }
}
@Serializable
data class LogCredentials(val credential: String, val password: String)