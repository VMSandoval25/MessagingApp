package app.getsizzle.web

import app.getsizzle.shared.Api.addMessage
import app.getsizzle.shared.Api.findChatRoomId
import app.getsizzle.shared.Api.findSenderID
import app.getsizzle.shared.Api.getMessage
import app.getsizzle.shared.Api.updateMessageRead
import app.getsizzle.shared.data.model.Message
import app.getsizzle.shared.displayDate
import app.getsizzle.shared.displayTime
import app.getsizzle.shared.socketClient
import csstype.ClassName
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.js.timers.setTimeout
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.p
import react.router.useLocation
import react.useEffectOnce
import react.useState

private val scope = MainScope()


val ChatPage = FC<Props> {

    var messageElement by useState(emptyList<Message>())
    var activeUser by useState("")
    var prevMessageDate:String? = ""
    var activePatient by useState("")
    var patient = useLocation()

    useEffectOnce {
        scope.launch {
            if(!socketClient._job.isActive){
                socketClient._job.start()
            }
        }
        scope.launch{
            activeUser = findSenderID()
            activePatient = patient.search.substring(1)
            messageElement = getMessage(findChatRoomId(findSenderID(),patient.search.substring(1)))
            while(true) {
                socketClient.chatMessage.collect { value ->
                    println(value)
                    console.log("websocket")
                    console.log(findSenderID())
                    console.log(patient.search.substring(1))
                    messageElement =
                        getMessage(findChatRoomId(findSenderID(), patient.search.substring(1)))
                    setTimeout(fun(){document.getElementById("chat-container")
                        ?.scrollTo(0.0, document.getElementById("chat-container")?.scrollHeight!!.toDouble())},400)
                }
            }
        }
    }
    div {
        className = ClassName("chat-container")
        id = "chat-container"
        messageElement.forEach { message ->
            val sender: String = if (message.senderID == activeUser) "me" else "other"
            val date: String? = message.sentTime?.let { it1 -> displayDate(it1) }
            val time: String? = message.sentTime?.let { it1 -> displayTime(it1) }

            if (date != prevMessageDate) {
                div {
                    className = ClassName("time-profile")
                    +"$date"
                }
                prevMessageDate = date
            }
            if (sender == "other") {
                div{
                    className = ClassName("chat-container-other")
                    div {
                        className = ClassName("profile-icon")
                        img {
                            src =
                                "/pics/default_icon.png"
                            alt = "profile-pic"
                        }
                    }
                    div {
                        className = ClassName("chat-bubble chat-bubble-$sender")
                        +message.messageText
                    }
                    div{
                        className = ClassName("timestamp-other")
                        +"${message.sentTime?.let { it1 -> displayTime(it1) }}"
                    }
                }
            } else {
                div {
                    className = ClassName("chat-container-me")
                    p {
                        className = ClassName("timestamp-me")
                        +"${message.sentTime?.let { it1 -> displayTime(it1) }}"
                    }
                    div {
                        className = ClassName("chat-bubble chat-bubble-$sender")
                        +message.messageText
                    }
                }
            }
            if (message.senderID != activeUser && !message.isRead) {
                scope.launch {
                    updateMessageRead(message)
                }
            }
        }
        setTimeout(fun(){document.getElementById("chat-container")
            ?.scrollTo(0.0, document.getElementById("chat-container")?.scrollHeight!!.toDouble())},400)
    }
    chatPageComponent {
        onSubmit = { input ->
            scope.launch {
                val message: Message = Message(
                    findChatRoomId(activeUser, activePatient),
                    input,
                    Clock.System.now(),
                    findSenderID(),
                    false
                )
                addMessage(message)
                messageElement = getMessage(findChatRoomId(activeUser, activePatient))
                socketClient.send(input)
            }
        }
    }
}