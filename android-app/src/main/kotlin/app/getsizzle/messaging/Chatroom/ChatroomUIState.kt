package app.getsizzle.messaging.Chatroom

import app.getsizzle.shared.data.model.Message


//data class Message(
//    val author: String="",
//    val content: String="",
//    //val timestamp: String="",
//    //val image: Int? = null
//    //val authorImage: Int = if (author == "me") R.drawable.ali else R.drawable.someone_else
//)

data class ChatroomUIState (
    val chatroomID: Int=0,
    val ownerID: Int=0,
    val channelMembers: Int=0,
    val typedMessage: String="",
    val initialMessages:List<Message>,
    val lastDate:String?=null
)
