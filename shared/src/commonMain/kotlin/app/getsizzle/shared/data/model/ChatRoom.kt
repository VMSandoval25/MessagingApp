package app.getsizzle.shared.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoom(
    val chatRoomId: Int,
    val dietitianId: String,
    val patientId: String,
){
    companion object {
        const val path = "/chatRoom"
    }
}