package app.getsizzle.shared.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(

    val chatRoomId: Int, // FK, unique identifier
    val messageText: String,
    val sentTime: Instant?,
    val senderID: String, // userId
    val isRead: Boolean,
){
    val messageId: Int = hashCode() // PK
    companion object {
        const val path = "/message"
    }
}