package app.getsizzle.messaging.Chatroom

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import app.getsizzle.messaging.BaseStateViewModel
import app.getsizzle.shared.data.model.Message
import app.getsizzle.shared.socketClient
import kotlinx.coroutines.flow.update

class ChatroomViewModel : BaseStateViewModel<ChatroomUIState, ChatroomViewModel.ChatroomEvent>(ChatroomUIState(initialMessages = emptyList())) {
    var lazyColumnState: LazyListState? = null

    var isCollecting = mutableStateOf(false)

    sealed class ChatroomEvent() {
        object ShowLastScreen : ChatroomEvent()
        object ShowProfileScreen : ChatroomEvent()
    }

    fun onProfileClicked() {
        sendEvent(ChatroomEvent.ShowProfileScreen)
        resetMessageField()
    }

    fun onBackClicked() {
        sendEvent(ChatroomEvent.ShowLastScreen)
        resetMessageField()
    }

    fun updateMessage(it: String) {
        _uiState.update { currentState ->
            currentState.copy(typedMessage = it)
        }
    }

    fun updateMessageList(messages: List<Message>) {
        _uiState.update { currentState ->
            currentState.copy(initialMessages = messages)
        }
    }

    fun addMessageList(message: Message) {
        _uiState.update { currentState ->
            currentState.copy(initialMessages = _uiState.value.initialMessages + message)
        }
    }

    fun resetMessageField() {
        _uiState.update { currentState ->
            currentState.copy(typedMessage = "")
        }
    }

    fun updateLastDate(currDate:String)
    {
        _uiState.update { currentState ->
            currentState.copy(lastDate = currDate)
        }
    }
//    fun isLastDate(currDate:Int): Boolean {
//       if(currDate==_uiState.value.lastDate)
//       {
//           updateLastDate(currDate)
//           return true
//       }
//        return false
//    }
    fun onMessageSent(username: String, messages: List<Message>)
    {
        // update database
        //update list of messages
        socketClient.send(_uiState.value.typedMessage)
        updateMessageList(messages)
        //Message(username,_uiState.value.typedMessage)


    }

}
