package app.getsizzle.messaging

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle


/**
 * @author Ryan Simon
 */
/**
 * Observe [BaseStateViewModel.event] in a Compose [LaunchedEffect].
 * @param lifecycleState [Lifecycle.State] in which [lifecycleState] block runs.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <CustomState,CustomEvent>
        BaseStateViewModel<CustomState, CustomEvent>.collectEvents(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    eventHandler: (suspend (event: CustomEvent) -> Unit)
) {
    val eventFlow = this.event
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(eventFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            eventFlow.collect {
                eventHandler(it)
            }
        }
    }
}


/**
 * Observe [BaseStateViewModel.state] as [State].
 * @param lifecycleState The Lifecycle where the restarting collecting from this flow work will be kept alive.
 */
@Composable
fun < CustomState,CustomEvent>
        BaseStateViewModel<CustomState, CustomEvent>.collectState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<CustomState> {
    return this.uiState.collectState(lifecycleState = lifecycleState)
}
