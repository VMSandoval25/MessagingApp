package app.getsizzle.messaging

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Ryan Simon
 */

/**
 * Safely observe a stateflow with a local lifecycle owner.
 *
 * @param lifecycleState The Lifecycle where the restarting collecting from this flow work will be kept alive.
 */
@Composable
fun <T> StateFlow<T>.collectState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current

    val stateFlowLifecycleAware = remember(this, lifecycleOwner) {
        this.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }

    // Need to access the initial value to convert to State - collectAsState() suppresses this lint warning too
    @SuppressLint("StateFlowValueCalledInComposition")
    val initialValue = this.value
    return stateFlowLifecycleAware.collectAsState(initialValue)
}
