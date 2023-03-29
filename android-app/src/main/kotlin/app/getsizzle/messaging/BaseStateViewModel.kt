package app.getsizzle.messaging

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStateViewModel<CustomState,CustomEvent>(initialState:CustomState) {
    protected val _uiState = MutableStateFlow(initialState) // c
    val uiState: StateFlow<CustomState> = _uiState
    private val _event = MutableSharedFlow<CustomEvent>(
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST // gotta use this in order to be able to use tryEmit function
    )

    val event: SharedFlow<CustomEvent> = _event
    fun sendEvent(event: CustomEvent) {
        _event.tryEmit(event)
    }

}